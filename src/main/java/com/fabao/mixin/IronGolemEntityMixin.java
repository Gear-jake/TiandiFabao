package com.fabao.mixin;

import com.fabao.entity.BaigujingEntity;
import com.fabao.entity.BearDemonEntity;
import com.fabao.entity.FoxDemonEntity;
import com.fabao.entity.JinjiaoDawangEntity;
import com.fabao.entity.SpiderDemonEntity;
import com.fabao.entity.YinjiaoDawangEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 铁傀儡主动攻击妖精（所有 fabao 的妖）。
 * 在 IronGolemEntity.initGoals 末尾追加目标选择器。
 */
@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin {
	@Inject(method = "initGoals", at = @At("TAIL"))
	private void fabao$targetDemons(CallbackInfo ci) {
		IronGolemEntity self = (IronGolemEntity) (Object) this;
		MobEntityAccessor accessor = (MobEntityAccessor) self;
		accessor.fabao$getTargetSelector().add(3, new ActiveTargetGoal<>(self, PathAwareEntity.class, true,
				(entity, world) -> entity instanceof FoxDemonEntity
						|| entity instanceof SpiderDemonEntity
						|| entity instanceof BearDemonEntity
						|| entity instanceof JinjiaoDawangEntity
						|| entity instanceof YinjiaoDawangEntity
						|| entity instanceof BaigujingEntity));
	}
}
