package com.fabao.mixin;

import com.fabao.entity.BaigujingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 白骨精伪装死亡时变回真身：
 * 带 fabao_bg_form_N_M 标签的实体死亡 -> 生成半血白骨精（继承 N/M 进度）。
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
	private void fabao$revertToTrue(DamageSource source, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (!(self.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}
		for (String tag : self.getCommandTags()) {
			if (!tag.startsWith(BaigujingEntity.FORM_TAG_PREFIX)) {
				continue;
			}
			String[] parts = tag.substring(BaigujingEntity.FORM_TAG_PREFIX.length()).split("_");
			if (parts.length != 2) {
				return;
			}
			int remaining;
			int mask;
			try {
				remaining = Integer.parseInt(parts[0]);
				mask = Integer.parseInt(parts[1]);
			} catch (NumberFormatException e) {
				return;
			}
			self.removeCommandTag(tag);
			BaigujingEntity.revertToTrue(world, self.getEntityPos(), remaining, mask);
			self.discard();
			ci.cancel();
			return;
		}
	}
}
