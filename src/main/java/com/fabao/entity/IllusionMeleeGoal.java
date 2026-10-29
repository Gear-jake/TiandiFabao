package com.fabao.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * 给「没有 ATTACK_DAMAGE 属性」的实体（如原版村民）用的近战攻击 Goal。
 * 绕过属性系统，直接在攻击命中时造成固定伤害，否则 MeleeAttackGoal 会算出 0 伤害。
 */
public class IllusionMeleeGoal extends MeleeAttackGoal {
	private final float damage;

	public IllusionMeleeGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle, float damage) {
		super(mob, speed, pauseWhenMobIdle);
		this.damage = damage;
	}

	@Override
	protected void attack(LivingEntity target) {
		PathAwareEntity mob = this.mob;
		if (!mob.getEntityWorld().isClient() && mob.getEntityWorld() instanceof ServerWorld server) {
			target.damage(server, server.getDamageSources().mobAttack(mob), this.damage);
		}
	}
}
