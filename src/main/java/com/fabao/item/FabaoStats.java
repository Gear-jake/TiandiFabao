package com.fabao.item;

import com.fabao.registry.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * 法宝组件读写与统一乘区公式（DESIGN §1）：
 * 伤害/被动倍率 = (1 + 0.1 × 品阶) × 认主且为主 ×1.2。
 */
public final class FabaoStats {
	public static final int MAX_TIER = 9;

	private FabaoStats() {
	}

	public static int tier(ItemStack stack) {
		return stack.getOrDefault(ModComponents.TIER, 0);
	}

	public static void setTier(ItemStack stack, int tier) {
		stack.set(ModComponents.TIER, Math.clamp(tier, 0, MAX_TIER));
	}

	public static boolean isBound(ItemStack stack) {
		return stack.contains(ModComponents.OWNER_UUID);
	}

	public static boolean isBoundTo(ItemStack stack, PlayerEntity player) {
		String uuid = stack.get(ModComponents.OWNER_UUID);
		return uuid != null && uuid.equals(player.getUuidAsString());
	}

	public static void bind(ItemStack stack, PlayerEntity player) {
		stack.set(ModComponents.OWNER_UUID, player.getUuidAsString());
		stack.set(ModComponents.OWNER_NAME, player.getName().getString());
	}

	public static int affix(ItemStack stack) {
		return stack.getOrDefault(ModComponents.AFFIX, 0);
	}

	public static float multiplier(ItemStack stack, PlayerEntity player) {
		float mult = 1.0f + 0.1f * tier(stack);
		mult *= 1.0f + affix(stack) / 100.0f;
		if (isBoundTo(stack, player)) {
			mult *= 1.2f;
		}
		return mult;
	}
}
