package com.fabao.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 暴露 MobEntity 的 goal 选择器，供幻影分身给被标记的村民补攻击 AI。
 */
@Mixin(MobEntity.class)
public interface MobEntityAccessor {
	@Accessor("goalSelector")
	GoalSelector fabao$getGoalSelector();

	@Accessor("targetSelector")
	GoalSelector fabao$getTargetSelector();
}
