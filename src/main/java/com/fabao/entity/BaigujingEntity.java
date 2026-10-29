package com.fabao.entity;

import com.fabao.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * 白骨精：女性骷髅妖，会「三变」。
 *
 * 循环：
 *   白骨精(满血) → 打死 → 伪装① → 打死 → 白骨精(半血)
 *   → 打死 → 伪装② → 打死 → 白骨精(半血)
 *   → 打死 → 伪装③ → 打死 → 白骨精(半血)
 *   → 打死 → 真死
 * 三次伪装的种类互不相同（村民 / 小村民 / 女巫各一次）。
 */
public class BaigujingEntity extends PathAwareEntity {
	/** 真身携带状态的标签：fabao_bg_state_<还能变几次>_<已用的位掩码>。 */
	public static final String STATE_TAG_PREFIX = "fabao_bg_state_";
	/** 伪装携带状态的标签：fabao_bg_form_<还能变几次>_<已用的位掩码>。 */
	public static final String FORM_TAG_PREFIX = "fabao_bg_form_";
	public static final int MAX_TRANSFORMS = 3;

	private int transformsLeft = MAX_TRANSFORMS;
	private int usedMask = 0;
	private boolean stateLoaded = false;
	private int poseTicks;

	public BaigujingEntity(EntityType<? extends PathAwareEntity> type, World world) {
		super(type, world);
		this.experiencePoints = 40;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return PathAwareEntity.createMobAttributes()
				.add(EntityAttributes.MAX_HEALTH, 120.0)
				.add(EntityAttributes.ATTACK_DAMAGE, 10.0)
				.add(EntityAttributes.ARMOR, 6.0)
				.add(EntityAttributes.MOVEMENT_SPEED, 0.26)
				.add(EntityAttributes.FOLLOW_RANGE, 24.0);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new net.minecraft.entity.ai.goal.SwimGoal(this));
		this.goalSelector.add(1, new net.minecraft.entity.ai.goal.MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(2, new net.minecraft.entity.ai.goal.WanderAroundGoal(this, 0.6));
		this.goalSelector.add(3, new net.minecraft.entity.ai.goal.LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
		this.goalSelector.add(4, new net.minecraft.entity.ai.goal.LookAroundGoal(this));
		this.targetSelector.add(1, new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(this, PlayerEntity.class, false));
		this.targetSelector.add(2, new net.minecraft.entity.ai.goal.RevengeGoal(this));
		this.targetSelector.add(3, new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(this, net.minecraft.entity.passive.IronGolemEntity.class, true));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getEntityWorld().isClient()) {
			return;
		}
		if (!stateLoaded) {
			loadState();
			stateLoaded = true;
		}
		if (poseTicks > 0) {
			poseTicks--;
			if (poseTicks == 0) {
				this.setPose(net.minecraft.entity.EntityPose.STANDING);
			}
		}
	}

	private void loadState() {
		for (String tag : this.getCommandTags()) {
			if (!tag.startsWith(STATE_TAG_PREFIX)) {
				continue;
			}
			String[] parts = tag.substring(STATE_TAG_PREFIX.length()).split("_");
			if (parts.length == 2) {
				try {
					transformsLeft = Integer.parseInt(parts[0]);
					usedMask = Integer.parseInt(parts[1]);
				} catch (NumberFormatException ignored) {
				}
			}
			this.removeCommandTag(tag);
			return;
		}
	}

	@Override
	public void onDeath(DamageSource source) {
		if (this.getEntityWorld() instanceof ServerWorld world && transformsLeft > 0) {
			int pick = pickUnused(world);
			if (pick >= 0) {
				spawnForm(world, pick, transformsLeft - 1, usedMask | (1 << pick), this.getEntityPos());
				this.discard();
				return;
			}
		}
		super.onDeath(source);
	}

	/** 选一个还没用过的伪装种类；都用过了返回 -1。 */
	private int pickUnused(ServerWorld world) {
		int[] candidates = new int[3];
		int n = 0;
		for (int i = 0; i < 3; i++) {
			if ((usedMask & (1 << i)) == 0) {
				candidates[n++] = i;
			}
		}
		if (n == 0) {
			return -1;
		}
		return candidates[world.random.nextInt(n)];
	}

	/** 生成伪装形态。0=村民 1=小村民 2=女巫。 */
	public static void spawnForm(ServerWorld world, int kind, int remaining, int mask, Vec3d at) {
		if (kind == 2) {
			WitchEntity w = EntityType.WITCH.create(world, SpawnReason.MOB_SUMMONED);
			if (w == null) {
				return;
			}
			w.setPosition(at.x, at.y, at.z);
			w.addCommandTag(FORM_TAG_PREFIX + remaining + "_" + mask);
			w.setPersistent();
			world.spawnEntity(w);
			return;
		}
		VillagerEntity v = EntityType.VILLAGER.create(world, SpawnReason.MOB_SUMMONED);
		if (v == null) {
			return;
		}
		v.setPosition(at.x, at.y, at.z);
		v.setBaby(kind == 1);
		v.addCommandTag(FORM_TAG_PREFIX + remaining + "_" + mask);
		v.setPersistent();
		world.spawnEntity(v);
	}

	/** 伪装被打死后变回白骨精（半血），继承变身进度。 */
	public static void revertToTrue(ServerWorld world, Vec3d at, int remaining, int mask) {
		BaigujingEntity real = ModEntities.BAIGUJING.create(world, SpawnReason.MOB_SUMMONED);
		if (real == null) {
			return;
		}
		real.setPosition(at.x, at.y, at.z);
		real.transformsLeft = remaining;
		real.usedMask = mask;
		real.stateLoaded = true;
		real.setHealth(real.getMaxHealth() * 0.5f);
		world.spawnEntity(real);
	}
}
