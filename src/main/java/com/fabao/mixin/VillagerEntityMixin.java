package com.fabao.mixin;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import com.fabao.entity.IllusionMeleeGoal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 被白骨精标记为分身的村民，自动获得攻击玩家的能力。
 * 用命令标签一次性武装，避免每 tick 重复添加 goal。
 */
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
	private static final String TAG_ILLUSION = com.fabao.entity.BaigujingEntity.FORM_TAG_PREFIX;
	private static final String TAG_ARMED = "fabao_illusion_armed";

	@Inject(method = "tick", at = @At("HEAD"))
	private void fabao$armIllusionVillager(CallbackInfo ci) {
		VillagerEntity self = (VillagerEntity) (Object) this;
		boolean isForm = self.getCommandTags().stream().anyMatch(t -> t.startsWith(TAG_ILLUSION));
		if (!isForm || self.getCommandTags().contains(TAG_ARMED)) {
			return;
		}
		self.addCommandTag(TAG_ARMED);
		MobEntityAccessor accessor = (MobEntityAccessor) self;
		accessor.fabao$getTargetSelector().add(1, new ActiveTargetGoal<>(self, PlayerEntity.class, false));
		accessor.fabao$getGoalSelector().add(1, new IllusionMeleeGoal(self, 1.0, false, 6.0f));
	}
}
