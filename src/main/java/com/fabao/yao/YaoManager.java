package com.fabao.yao;

import com.fabao.registry.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 妖体系总控：
 * - 召唤：妖丹右键在身前召唤对应等级的妖（原版实体模板 + 妖名 + 属性重调 + tag 标记）；
 * - 机制：每 20 tick 扫描玩家 16 格内带 tag 的妖，按实体 age 周期触发各自机制；
 * - 掉落：AFTER_DEATH 按 tag 精确掉落妖丹，不污染原版战利品表。
 */
public final class YaoManager {
	public static final String TAG_PREFIX = "fabao:yao_";
	private static final double SCAN_RANGE = 16.0;
	private static final Set<UUID> SEEN = new HashSet<>();

	private YaoManager() {
	}

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			YaoType type = typeOf(entity);
			if (type == null) {
				return;
			}
			entity.dropStack((ServerWorld) entity.getEntityWorld(),
					new ItemStack(danFor(type.tier())));
		});
		ServerTickEvents.END_SERVER_TICK.register(YaoManager::onEndTick);
	}

	public static void summon(ServerPlayerEntity player, int tier) {
		java.util.List<YaoType> pool = YaoType.byTier(tier);
		if (pool.isEmpty()) {
			com.fabao.util.Feedback.fail(player, "fabao.yao.fail");
			return;
		}
		YaoType type = pool.get(player.getRandom().nextInt(pool.size()));
		spawn(player, type);
	}

	/** 潜行右键：按 tier 顺序循环召唤（狐妖 → 蜘蛛精 → 黑熊精 → 循环），便于逐个查看模型。 */
	private static final java.util.Map<UUID, Integer> CYCLE = new java.util.HashMap<>();

	public static void summonCycle(ServerPlayerEntity player, int tier) {
		java.util.List<YaoType> pool = YaoType.byTier(tier);
		if (pool.isEmpty()) {
			com.fabao.util.Feedback.fail(player, "fabao.yao.fail");
			return;
		}
		int idx = CYCLE.getOrDefault(player.getUuid(), 0) % pool.size();
		YaoType type = pool.get(idx);
		CYCLE.put(player.getUuid(), idx + 1);
		spawn(player, type);
	}

	/** 实际生成一只指定妖。 */
	public static void spawn(ServerPlayerEntity player, YaoType type) {
		ServerWorld world = player.getEntityWorld();
		Entity entity = type.template().create(world, SpawnReason.MOB_SUMMONED);
		if (entity == null) {
			return;
		}
		Vec3d look = player.getRotationVec(1.0f);
		double x = player.getX() + look.x * 3.0;
		double z = player.getZ() + look.z * 3.0;
		entity.setPosition(x, player.getY(), z);
		entity.setCustomName(Text.translatable("fabao.yao." + type.id()));
		entity.setCustomNameVisible(true);
		entity.addCommandTag(TAG_PREFIX + type.id());
		entity.addCommandTag(TAG_PREFIX + "tier_" + type.tier());
		if (entity instanceof LivingEntity living) {
			if (living.getAttributeInstance(EntityAttributes.MAX_HEALTH) != null) {
				living.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(type.maxHealth());
			}
			living.setHealth(type.maxHealth());
			if (living.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE) != null) {
				living.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(type.attackDamage());
			}
			if (living instanceof MobEntity mob) {
				mob.setPersistent();
				mob.setTarget(player);
			}
		}
		world.spawnEntity(entity);
		com.fabao.util.Feedback.success(player, "fabao.yao.summoned", entity.getCustomName());
	}

	public static void onEndTick(MinecraftServer server) {
		if (server.getTicks() % 20 != 0) {
			return;
		}
		SEEN.clear();
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			ServerWorld world = player.getEntityWorld();
			Box box = player.getBoundingBox().expand(SCAN_RANGE, 8.0, SCAN_RANGE);
			for (Entity candidate : world.getOtherEntities(player, box,
					e -> e instanceof LivingEntity && e.getCommandTags().stream().anyMatch(t -> t.startsWith(TAG_PREFIX)))) {
				if (!(candidate instanceof LivingEntity living) || !living.isAlive() || !SEEN.add(living.getUuid())) {
					continue;
				}
				YaoType type = typeOf(living);
				if (type != null) {
					type.tickMechanic(living, player);
				}
			}
		}
	}

	// ---- 妖的特殊机制（还原原著） ----

	/** 狐妖·魅惑：周期性魅惑附近玩家（失明）。 */
	public static void mechCharm(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 100 == 0 && self.squaredDistanceTo(player) < 64.0) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
		}
	}

	/** 蜘蛛精·织网：在玩家脚下织出蛛网。 */
	public static void mechWeave(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 160 == 0 && self.squaredDistanceTo(player) < 256.0) {
			ServerWorld world = (ServerWorld) self.getEntityWorld();
			world.setBlockState(player.getBlockPos(), Blocks.COBWEB.getDefaultState());
		}
	}

	/** 黑熊精·蛮力冲锋。 */
	public static void mechCharge(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 120 == 0 && self.squaredDistanceTo(player) < 400.0) {
			Vec3d dir = player.getEntityPos().subtract(self.getEntityPos()).normalize();
			self.addVelocity(dir.x * 1.2, 0.3, dir.z * 1.2);
		}
	}

	/** 白骨精·吸精：持续回血。 */
	public static void mechDrain(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 20 == 0 && self.getHealth() < self.getMaxHealth()) {
			self.heal(1.0f);
		}
	}

	/** 金角大王·紫金红葫芦：将玩家吸向自己并施加缓慢。 */
	public static void mechGourd(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 200 == 0 && self.squaredDistanceTo(player) < 100.0) {
			Vec3d pull = self.getEntityPos().subtract(player.getEntityPos()).normalize().multiply(0.6);
			player.addVelocity(pull.x, 0.2, pull.z);
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
		}
	}

	/** 银角大王·晃金绳：束缚玩家。 */
	public static void mechRope(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 160 == 0 && self.squaredDistanceTo(player) < 144.0) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 3));
		}
	}

	/** 六耳猕猴·幻术：自身隐身 + 玩家失明，真假难辨。 */
	public static void mechIllusion(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 240 == 0 && self.squaredDistanceTo(player) < 256.0) {
			self.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 60, 0));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0));
		}
	}

	/** 金翅大鹏·俯冲：高速冲击玩家并击飞。 */
	public static void mechDive(LivingEntity self, ServerPlayerEntity player) {
		if (self.age % 120 == 0 && self.squaredDistanceTo(player) < 900.0) {
			Vec3d dir = player.getEntityPos().subtract(self.getEntityPos()).normalize();
			self.addVelocity(dir.x * 2.0, dir.y + 0.4, dir.z * 2.0);
		}
	}

	private static YaoType typeOf(Entity entity) {
		for (String tag : entity.getCommandTags()) {
			if (tag.startsWith(TAG_PREFIX) && !tag.contains("tier_")) {
				return YaoType.byId(tag.substring(TAG_PREFIX.length()));
			}
		}
		return null;
	}

	private static net.minecraft.item.Item danFor(int tier) {
		return switch (tier) {
			case 1 -> ModItems.XIAOYAO_DAN;
			case 2 -> ModItems.DAYAO_DAN;
			default -> ModItems.YAOWANG_DAN;
		};
	}
}
