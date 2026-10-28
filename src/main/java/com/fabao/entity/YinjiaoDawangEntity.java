package com.fabao.entity;

import com.fabao.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * 银角大王：80 血 / 8 攻击 / 8 护甲。
 * 特殊攻击「羊脂玉净瓶」：把 6 格内最近的玩家收进瓶中，
 * 受到缓慢与虚弱，5 秒后放出。
 */
public class YinjiaoDawangEntity extends PathAwareEntity {
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

	public YinjiaoDawangEntity(EntityType<? extends PathAwareEntity> type, World world) {
		super(type, world);
		this.experiencePoints = 45;
		this.cooldown = SPECIAL_COOLDOWN;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return PathAwareEntity.createMobAttributes()
				.add(EntityAttributes.MAX_HEALTH, 80.0)
				.add(EntityAttributes.ATTACK_DAMAGE, 8.0)
				.add(EntityAttributes.ARMOR, 8.0)
				.add(EntityAttributes.MOVEMENT_SPEED, 0.25)
				.add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.4)
				.add(EntityAttributes.FOLLOW_RANGE, 24.0);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new net.minecraft.entity.ai.goal.SwimGoal(this));
		this.goalSelector.add(1, new net.minecraft.entity.ai.goal.MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(2, new net.minecraft.entity.ai.goal.WanderAroundGoal(this, 0.6));
		this.goalSelector.add(3, new net.minecraft.entity.ai.goal.LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
		this.goalSelector.add(4, new net.minecraft.entity.ai.goal.LookAroundGoal(this));
		this.targetSelector.add(1, new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(2, new net.minecraft.entity.ai.goal.RevengeGoal(this));
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

	private void trySpecial() {
		if (!(this.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}
		PlayerEntity target = world.getClosestPlayer(this, 6.0);
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
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, CAPTURE_TICKS + 10, 6, false, false));
		this.setPose(net.minecraft.entity.EntityPose.LONG_JUMPING);
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
		if (player.getVehicle() != this) {
			player.startRiding(this, true, true);
		}
		if (captureTicks % 20 == 0) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 50, 0, false, true));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 50, 0, false, true));
		}
		captureTicks--;
		if (captureTicks <= 0) {
			release(false);
		}
	}

	private void release(boolean teleportBack) {
		if (captured != null && this.getEntityWorld() instanceof ServerWorld world) {
			ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(captured);
			if (player != null) {
				player.stopRiding();
				player.setInvisible(false);
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
		this.setPose(net.minecraft.entity.EntityPose.STANDING);
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

	public boolean isCasting() {
		return captured != null;
	}
}
