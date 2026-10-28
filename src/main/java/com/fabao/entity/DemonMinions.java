package com.fabao.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

/**
 * 金角/银角大王的召唤小怪辅助：在自身周围生成指定等级的妖，并锁定同一目标。
 * 用命令标签标记归属，避免超过上限或互相抢目标。
 */
public final class DemonMinions {
	public static final int MAX_MINIONS = 10;
	public static final int SUMMON_COOLDOWN = 200;

	private DemonMinions() {
	}

	private static String tagFor(PathAwareEntity owner) {
		return "fabao_minion_" + owner.getUuidAsString().substring(0, 8);
	}

	/** 当前属于该妖的小怪数量（16 格内）。 */
	public static int count(PathAwareEntity owner) {
		if (!(owner.getEntityWorld() instanceof ServerWorld world)) {
			return 0;
		}
		String tag = tagFor(owner);
		return world.getEntitiesByClass(PathAwareEntity.class, owner.getBoundingBox().expand(16.0), e ->
				e != owner && e.getCommandTags().contains(tag)).size();
	}

	/** 一次召唤多只，返回实际生成数量。 */
	public static int summonBatch(PathAwareEntity owner, EntityType<? extends PathAwareEntity> type, int wanted) {
		int made = 0;
		for (int i = 0; i < wanted; i++) {
			if (!summonOne(owner, type)) {
				break;
			}
			made++;
		}
		return made;
	}

	/** 尝试召唤一只小怪，成功返回 true。 */
	public static boolean summonOne(PathAwareEntity owner, EntityType<? extends PathAwareEntity> type) {
		if (!(owner.getEntityWorld() instanceof ServerWorld world)) {
			return false;
		}
		if (count(owner) >= MAX_MINIONS) {
			return false;
		}
		PathAwareEntity minion = type.create(world, SpawnReason.MOB_SUMMONED);
		if (minion == null) {
			return false;
		}
		Vec3d base = owner.getEntityPos();
		double angle = world.random.nextDouble() * Math.PI * 2;
		double dist = 1.6 + world.random.nextDouble() * 3.0;
		minion.setPosition(base.x + Math.cos(angle) * dist, base.y, base.z + Math.sin(angle) * dist);
		minion.addCommandTag(tagFor(owner));
		minion.addCommandTag("fabao_minion");
		if (minion instanceof MobEntity mob) {
			mob.setPersistent();
			LivingEntity target = owner instanceof MobEntity ownerMob ? ownerMob.getTarget() : null;
			if (target != null) {
				mob.setTarget(target);
			}
		}
		world.spawnEntity(minion);
		return true;
	}
}
