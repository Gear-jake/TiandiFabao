package com.fabao.entity;

import com.fabao.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * 金角大王：100 血 / 9 攻击 / 10 护甲。
 * 特殊攻击「紫金红葫芦」：把 8 格内最近的玩家吸进葫芦，
 * 被收进去的人持续受中毒 I + 凋零 I，5 秒后放出。
 */
public class JinjiaoDawangEntity extends PathAwareEntity {
	private static final int CAPTURE_TICKS = 100;
	private static final int SPECIAL_COOLDOWN = 300;

	private UUID captured;
	private int captureTicks;
	private int cooldown;
	private int minionCooldown = DemonMinions.SUMMON_COOLDOWN;
	private int attackTicks;
	private double releaseX;
	private double releaseY;
	private double releaseZ;

	public JinjiaoDawangEntity(EntityType<? extends PathAwareEntity> type, World world) {
		super(type, world);
		this.experiencePoints = 50;
		this.cooldown = SPECIAL_COOLDOWN;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return PathAwareEntity.createMobAttributes()
				.add(EntityAttributes.MAX_HEALTH, 100.0)
				.add(EntityAttributes.ATTACK_DAMAGE, 9.0)
				.add(EntityAttributes.ARMOR, 10.0)
				.add(EntityAttributes.MOVEMENT_SPEED, 0.23)
				.add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.5)
				.add(EntityAttributes.FOLLOW_RANGE, 24.0);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(2, new WanderAroundGoal(this, 0.6));
		this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(2, new RevengeGoal(this));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getEntityWorld().isClient()) {
			return;
		}
		if (cooldown > 0) {
			cooldown--;
		}
		if (attackTicks > 0) {
			attackTicks--;
		}
		if (minionCooldown > 0) {
			minionCooldown--;
		} else if (this.getTarget() != null) {
			int batch = 5 + this.random.nextInt(2);
			if (DemonMinions.summonBatch(this, randomMinionType(), batch) > 0) {
				minionCooldown = DemonMinions.SUMMON_COOLDOWN;
			} else {
				minionCooldown = 40;
			}
		}
		if (captured != null) {
			tickCapture();
		} else if (cooldown <= 0) {
			trySpecial();
		}
	}

	/** 特殊攻击：找 8 格内最近的玩家吸进来。 */
	private void trySpecial() {
		if (!(this.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}
		PlayerEntity target = world.getClosestPlayer(this, 8.0);
		if (target == null || !target.isAlive() || target.isCreative() || target.isSpectator()) {
			return;
		}
		if (!(target instanceof ServerPlayerEntity player)) {
			return;
		}
		captured = player.getUuid();
		captureTicks = CAPTURE_TICKS;
		cooldown = SPECIAL_COOLDOWN + CAPTURE_TICKS;
		releaseX = player.getX();
		releaseY = player.getY();
		releaseZ = player.getZ();
		player.startRiding(this, true, true);
		player.setInvisible(true);
		this.setPose(net.minecraft.entity.EntityPose.LONG_JUMPING);
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, CAPTURE_TICKS + 10, 6, false, false));
	}

	private void tickCapture() {
		if (!(this.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}
		ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(captured);
		if (player == null || !player.isAlive()) {
			release(true);
			return;
		}
		// 锁在葫芦里：掉下坐骑就立刻骑回去
		if (player.getVehicle() != this) {
			player.startRiding(this, true, true);
		}
		if (captureTicks % 20 == 0) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 50, 0, false, true));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 50, 0, false, true));
		}
		captureTicks--;
		if (captureTicks <= 0) {
			release(false);
		}
	}

	/** 放出被收的玩家，回到被抓的位置。 */
	private void release(boolean teleportBack) {
		if (captured != null && this.getEntityWorld() instanceof ServerWorld world) {
			ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(captured);
			if (player != null) {
				player.stopRiding();
				player.setInvisible(false);
				this.setPose(net.minecraft.entity.EntityPose.STANDING);
				player.removeStatusEffect(StatusEffects.SLOWNESS);
				if (teleportBack) {
					player.requestTeleport(releaseX, releaseY, releaseZ);
				} else {
					player.requestTeleport(this.getX() + 1.5, this.getY(), this.getZ());
				}
			}
		}
		captured = null;
		captureTicks = 0;
	}


	/** 随机挑一种小怪来召唤。 */
	private net.minecraft.entity.EntityType<? extends PathAwareEntity> randomMinionType() {
		return switch (this.random.nextInt(3)) {
			case 0 -> ModEntities.FOX_DEMON;
			case 1 -> ModEntities.SPIDER_DEMON;
			default -> ModEntities.BEAR_DEMON;
		};
	}

	@Override
	public boolean tryAttack(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.Entity target) {
		boolean hit = super.tryAttack(world, target);
		if (hit) {
			this.attackTicks = 12;
		}
		return hit;
	}

	public int getAttackTicks() {
		return attackTicks;
	}

	/** 正在施法（吸人入葫芦）——驱动 special 动画。 */
	public boolean isCasting() {
		return captured != null;
	}

	public boolean hasCaptured() {
		return captured != null;
	}

	/** 处于施法（吸人）状态的进度 0..1，供渲染层使用。 */
	public float captureProgress() {
		if (captured == null) {
			return 0f;
		}
		return 1f - (captureTicks / (float) CAPTURE_TICKS);
	}
}
