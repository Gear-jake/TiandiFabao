package com.fabao.equipment;

import com.fabao.FabaoMod;
import com.fabao.combat.ComboTracker;
import com.fabao.combat.QilingManager;
import com.fabao.cultivation.CultivationManager;
import com.fabao.item.FabaoItem;
import com.fabao.item.FabaoStats;
import com.fabao.item.FabaoType;
import com.fabao.registry.ModSounds;
import com.fabao.util.ParticleFx;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 法宝主动释放总控。
 * - 法器（饰品槽）：轮盘选择 → release(player, slot)，按槽位独立冷却；
 * - 兵器（手持）：右键按住蓄力 → onStoppedUsing → releaseHeld(player, type, ratio)，按玩家冷却。
 * 两条路径共用 cast()：灵力扣减 + 统一乘区 + 特效分派 + 连招记录。
 */
public final class FabaoActivator {
	private static final int COOLDOWN_TICKS = 40;
	private static final Map<UUID, int[]> SLOT_COOLDOWNS = new HashMap<>();
	private static final Map<UUID, Integer> HELD_COOLDOWNS = new HashMap<>();

	private FabaoActivator() {
	}

	/** 轮盘释放：先切到指定槽，再走普通释放流程。 */
	public static void release(ServerPlayerEntity player, int slotIndex) {
		if (slotIndex >= 0 && slotIndex < FabaoEquipment.equippedCount(player)) {
			FabaoEquipment.setState(player, new FabaoSlots(slotIndex));
		}
		release(player);
	}

	/** 法器（饰品槽）释放。 */
	public static void release(ServerPlayerEntity player) {
		ItemStack stack = FabaoEquipment.selectedStack(player);
		if (!(stack.getItem() instanceof FabaoItem fabao) || fabao.fabaoType().isWeapon()) {
			com.fabao.util.Feedback.fail(player, "fabao.slot.empty");
			return;
		}
		FabaoType type = fabao.fabaoType();
		int slot = FabaoEquipment.getState(player).selected();
		int remaining = slotCooldownRemaining(player, slot);
		if (remaining > 0) {
			com.fabao.util.Feedback.fail(player, "fabao.cooldown.remaining", remaining / 20.0f);
			return;
		}
		float ratio = ChargeLogic.consumeChargeRatio(player);
		if (cast(player, type, stack, ratio)) {
			setSlotCooldown(player, slot, player.getEntityWorld().getServer().getTicks());
		}
	}

	/** 兵器（手持）释放：按住右键蓄力，松开时由 onStoppedUsing 调用。 */
	public static void releaseHeld(ServerPlayerEntity player, FabaoItem item, float ratio) {
		ItemStack stack = player.getMainHandStack();
		if (!(stack.getItem() instanceof FabaoItem held) || held != item || !item.fabaoType().isWeapon()) {
			return;
		}
		int remaining = heldCooldownRemaining(player);
		if (remaining > 0) {
			com.fabao.util.Feedback.fail(player, "fabao.cooldown.remaining", remaining / 20.0f);
			return;
		}
		if (cast(player, item.fabaoType(), stack, ratio)) {
			HELD_COOLDOWNS.put(player.getUuid(),
					player.getEntityWorld().getServer().getTicks() + COOLDOWN_TICKS);
		}
	}

	/** 两条释放路径共用：灵力 → 伤害 → 特效 → 连招。返回是否成功发动（用于冷却与提示）。 */
	private static boolean cast(ServerPlayerEntity player, FabaoType type, ItemStack stack, float ratio) {
		boolean fullCharge = ratio >= 1.0f;
		boolean summonMode = fullCharge && player.isSneaking();
		if (!CultivationManager.tryConsume(player, type.spiritCost() + (summonMode ? 30 : 0))) {
			return false;
		}
		float chargeMult = fullCharge ? 2.5f : 0.5f + 1.5f * ratio;
		float damage = type.baseDamage() * FabaoStats.multiplier(stack, player) * chargeMult;
		ServerWorld world = player.getEntityWorld();
		FabaoMod.LOGGER.info("[Fabao] cast {} ratio={} damage={}", type.id(), ratio, damage);
		world.playSound(null, player.getX(), player.getY(), player.getZ(),
				ModSounds.ZHONGQING_RELEASE, SoundCategory.PLAYERS, 1.0f, fullCharge ? 0.6f : 1.0f);
		com.fabao.util.Feedback.success(player, "fabao.cast.ok", stack.getName());
		if (summonMode) {
			QilingManager.summon(player, stack, type.baseDamage() * FabaoStats.multiplier(stack, player) * 0.6f);
		} else {
			applyEffect(player, world, type, damage, fullCharge);
			ComboTracker.record(player, type);
		}
		return true;
	}

	private static void applyEffect(ServerPlayerEntity player, ServerWorld world, FabaoType type,
									float damage, boolean fullCharge) {
		Vec3d eye = player.getEntityPos().add(0, 1.2, 0);
		switch (type) {
			// 乾坤圈·掷出必中：金线飞射单体，命中爆点后回旋击退
			case QIANKUN_QUAN -> {
				LivingEntity target = nearestHostile(player, world, 20.0);
				if (target != null) {
					Vec3d hit = target.getEntityPos().add(0, target.getHeight() * 0.5, 0);
					ParticleFx.line(world, eye, hit, 12, ParticleFx.GOLD);
					hurtWithBonus(player, world, type, target, damage, false);
					ParticleFx.burst(world, target, ParticleFx.GOLD);
					Vec3d dir = target.getEntityPos().subtract(player.getEntityPos()).normalize();
					target.addVelocity(dir.multiply(fullCharge ? 2.4 : 1.4).add(0, 0.45, 0));
					world.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.9f, 1.5f);
				} else {
					ParticleFx.dust(world, eye.add(player.getRotationVec(1.0f).multiply(4.0)),
							ParticleFx.GOLD, 6, 0.4);
				}
			}
			// 金箍棒·万钧横扫：前方 5 格扇形金弧 + 击飞
			case RUYI_JINGU_BANG -> {
				ParticleFx.cone(world, player, 5.0, 48, ParticleFx.GOLD);
				ParticleFx.ring(world, player.getEntityPos(), 5.0, 24, ParticleFx.GOLD);
				for (LivingEntity enemy : inCone(player, world, 5.0)) {
					hurtWithBonus(player, world, type, enemy, damage * 1.5f, false);
					ParticleFx.burst(world, enemy, ParticleFx.GOLD);
					Vec3d dir = enemy.getEntityPos().subtract(player.getEntityPos()).normalize();
					enemy.addVelocity(dir.multiply(1.8).add(0, 0.5, 0));
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ITEM_MACE_SMASH_GROUND, SoundCategory.PLAYERS, 1.0f, 0.9f);
			}
			// 混天绫·缚仙索：前方 8 格红色缠绕，减速 + 虚弱 + 拉扯
			case HUNTIAN_LING -> {
				ParticleFx.cone(world, player, 8.0, 44, ParticleFx.RED);
				for (LivingEntity enemy : inCone(player, world, 8.0)) {
					hurtWithBonus(player, world, type, enemy, damage, false);
					ParticleFx.burst(world, enemy, ParticleFx.RED);
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 6));
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 2));
					Vec3d pull = player.getEntityPos().subtract(enemy.getEntityPos()).normalize().multiply(0.7);
					enemy.addVelocity(pull);
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.8f, 0.7f);
			}
			// 打神鞭·专打元神：前方 10 格雷链（≤5），魔法伤害无视护甲
			case DASHEN_BIAN -> {
				ParticleFx.cone(world, player, 10.0, 44, ParticleFx.THUNDER);
				int count = 0;
				for (LivingEntity enemy : inCone(player, world, 10.0)) {
					if (++count > 5) {
						break;
					}
					hurtWithBonus(player, world, type, enemy, damage, true);
					ParticleFx.burst(world, enemy, ParticleFx.THUNDER);
					spawnCosmeticBolt(world, enemy);
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0f, 1.2f);
			}
			// 紫金红葫芦·喊名收人：前方 8 格紫色漩涡，吸入身前并中毒
			case ZIJIN_HULU -> {
				ParticleFx.cone(world, player, 8.0, 44, ParticleFx.PURPLE);
				for (LivingEntity enemy : inCone(player, world, 8.0)) {
					hurtWithBonus(player, world, type, enemy, damage, false);
					ParticleFx.burst(world, enemy, ParticleFx.PURPLE);
					ParticleFx.line(world, enemy.getEntityPos().add(0, 0.6, 0), eye, 8, ParticleFx.PURPLE);
					Vec3d pull = player.getEntityPos().subtract(enemy.getEntityPos()).normalize().multiply(1.6);
					enemy.addVelocity(pull.add(0, 0.25, 0));
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, fullCharge ? 1 : 0));
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 0));
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1.0f, 0.5f);
			}
			// 金刚琢·套走兵器：前方 6 格白环扩散，缴械 + 眩晕
			case JINGANG_ZHUO -> {
				ParticleFx.ring(world, player.getEntityPos(), 6.0, 30, ParticleFx.WHITE);
				ParticleFx.cone(world, player, 6.0, 36, ParticleFx.WHITE);
				for (LivingEntity enemy : inCone(player, world, 6.0)) {
					hurtWithBonus(player, world, type, enemy, damage, false);
					ParticleFx.burst(world, enemy, ParticleFx.WHITE);
					ItemStack weapon = enemy.getMainHandStack();
					if (!weapon.isEmpty()) {
						enemy.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
						enemy.dropStack(world, weapon.copy());
					}
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 4));
					enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 2));
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1.0f, 0.8f);
			}
			// 斩妖刀·单体斩：白刃直线
			case ZHANYAO_DAO -> {
				LivingEntity target = nearestHostile(player, world, 5.0);
				if (target != null) {
					ParticleFx.line(world, eye, target.getEntityPos().add(0, target.getHeight() * 0.5, 0),
							8, ParticleFx.WHITE);
					hurtWithBonus(player, world, type, target, damage, false);
					ParticleFx.burst(world, target, ParticleFx.WHITE);
					Vec3d dir = target.getEntityPos().subtract(player.getEntityPos()).normalize();
					target.addVelocity(dir.multiply(0.8).add(0, 0.2, 0));
				} else {
					ParticleFx.cone(world, player, 5.0, 24, ParticleFx.WHITE);
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.9f, 1.8f);
			}
			// 降妖杵·震荡：橙环扩散 + 震退
			case JIANGYAO_CHU -> {
				ParticleFx.ring(world, player.getEntityPos(), 4.5, 26, ParticleFx.ORANGE);
				ParticleFx.cone(world, player, 4.5, 30, ParticleFx.ORANGE);
				for (LivingEntity enemy : inCone(player, world, 4.5)) {
					hurtWithBonus(player, world, type, enemy, damage, false);
					ParticleFx.burst(world, enemy, ParticleFx.ORANGE);
					Vec3d dir = enemy.getEntityPos().subtract(player.getEntityPos()).normalize();
					enemy.addVelocity(dir.multiply(1.2).add(0, 0.3, 0));
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ITEM_MACE_SMASH_GROUND, SoundCategory.PLAYERS, 1.0f, 1.1f);
			}
			// 定海神针·天倾：光柱 + 落雷 + 击飞
			case DINGHAI_SHENZHEN -> {
				ParticleFx.ring(world, player.getEntityPos(), 7.0, 36, ParticleFx.GOLD);
				for (LivingEntity enemy : inCone(player, world, 7.0)) {
					hurtWithBonus(player, world, type, enemy, damage, false);
					ParticleFx.column(world, enemy.getEntityPos(), 6.0, ParticleFx.THUNDER);
					ParticleFx.burst(world, enemy, ParticleFx.GOLD);
					Vec3d dir = enemy.getEntityPos().subtract(player.getEntityPos()).normalize();
					enemy.addVelocity(dir.multiply(2.0).add(0, 0.7, 0));
					spawnCosmeticBolt(world, enemy);
				}
				world.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.PLAYERS, 1.2f, 0.8f);
			}
		}
	}

	/** 伤害结算：对妖按 FabaoType.yaoBonus 加成；magic=true 时无视护甲且保留玩家击杀归属。 */
	private static void hurtWithBonus(ServerPlayerEntity player, ServerWorld world, FabaoType type,
									  LivingEntity target, float damage, boolean magic) {
		float finalDamage = damage * (isYao(target) ? 1.0f + type.yaoBonus() : 1.0f);
		if (magic) {
			target.damage(world, world.getDamageSources().indirectMagic(player, player), finalDamage);
		} else {
			target.damage(world, world.getDamageSources().playerAttack(player), finalDamage);
		}
	}

	public static boolean isYao(LivingEntity entity) {
		for (String tag : entity.getCommandTags()) {
			if (tag.startsWith(com.fabao.yao.YaoManager.TAG_PREFIX)) {
				return true;
			}
		}
		return false;
	}

	public static void hurt(ServerPlayerEntity player, ServerWorld world, LivingEntity target, float damage) {
		target.damage(world, world.getDamageSources().playerAttack(player), damage);
	}

	public static void spawnCosmeticBolt(ServerWorld world, LivingEntity target) {
		LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
		bolt.setCosmetic(true);
		bolt.setPosition(target.getX(), target.getY(), target.getZ());
		world.spawnEntity(bolt);
	}

	/** 范围内且位于玩家前方 ±60° 的敌对生物，按距离排序。 */
	public static List<LivingEntity> inCone(ServerPlayerEntity player, ServerWorld world, double radius) {
		List<LivingEntity> out = new ArrayList<>();
		for (LivingEntity enemy : hostilesAround(player, world, radius)) {
			if (inFrontCone(player, enemy)) {
				out.add(enemy);
			}
		}
		out.sort(Comparator.comparingDouble(e -> e.squaredDistanceTo(player)));
		return out;
	}

	public static List<LivingEntity> hostilesAround(ServerPlayerEntity player, ServerWorld world, double radius) {
		Box box = player.getBoundingBox().expand(radius, 4.0, radius);
		List<LivingEntity> out = new ArrayList<>();
		// 对所有生物生效（怪物 / 动物 / 自定义妖精），只排除玩家自己与其他玩家
		for (Entity entity : world.getOtherEntities(player, box,
				e -> e instanceof LivingEntity living && living.isAlive()
						&& !(living instanceof net.minecraft.entity.player.PlayerEntity))) {
			out.add((LivingEntity) entity);
		}
		return out;
	}

	public static LivingEntity nearestHostile(ServerPlayerEntity player, ServerWorld world, double radius) {
		return hostilesAround(player, world, radius).stream()
				.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(player)))
				.orElse(null);
	}

	/** 视线锥：目标方向与视线夹角 < 60°（cos = 0.5）。 */
	private static boolean inFrontCone(ServerPlayerEntity player, LivingEntity target) {
		Vec3d look = player.getRotationVec(1.0f).normalize();
		Vec3d to = target.getEntityPos().subtract(player.getEntityPos());
		if (to.lengthSquared() < 1.0E-4) {
			return true;
		}
		return look.dotProduct(to.normalize()) > 0.5;
	}

	private static int slotCooldownRemaining(ServerPlayerEntity player, int slot) {
		int[] arr = SLOT_COOLDOWNS.get(player.getUuid());
		if (arr == null) {
			return 0;
		}
		return Math.max(0, arr[slot] - player.getEntityWorld().getServer().getTicks());
	}

	private static int heldCooldownRemaining(ServerPlayerEntity player) {
		Integer ready = HELD_COOLDOWNS.get(player.getUuid());
		if (ready == null) {
			return 0;
		}
		return Math.max(0, ready - player.getEntityWorld().getServer().getTicks());
	}

	private static void setSlotCooldown(ServerPlayerEntity player, int slot, int nowTick) {
		SLOT_COOLDOWNS.computeIfAbsent(player.getUuid(), k -> new int[FabaoSlots.SLOT_COUNT])[slot] =
				nowTick + COOLDOWN_TICKS;
	}
}
