package com.fabao.cultivation;

import com.fabao.equipment.FabaoEquipment;
import com.fabao.registry.ModAttachments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * 灵力唯一入口。境界/渡劫已移除（P10）：灵力上限固定，回复随装配法宝数提升，
 * 法宝主动技、连招、器灵都从这里扣。
 */
public final class CultivationManager {
	public static final int MAX_SPIRIT = 100;

	private CultivationManager() {
	}

	public static CultivationData get(ServerPlayerEntity player) {
		return player.getAttachedOrCreate(ModAttachments.CULTIVATION);
	}

	private static void set(ServerPlayerEntity player, CultivationData data) {
		player.setAttached(ModAttachments.CULTIVATION, data);
	}

	public static int equippedCount(LivingEntity player) {
		return FabaoEquipment.equippedCount(player);
	}

	/** 灵力扣减：不足则提示（当前/所需）并返回 false，调用方不得执行技能。 */
	public static boolean tryConsume(ServerPlayerEntity player, int cost) {
		CultivationData data = get(player);
		if (data.spirit() < cost) {
			com.fabao.util.Feedback.fail(player, "fabao.spirit.empty.detail", data.spirit(), cost);
			return false;
		}
		set(player, data.withSpirit(data.spirit() - cost));
		return true;
	}

	/**
	 * 每秒（20 tick）：灵力回复 + actionbar HUD。
	 * 回复 = 2 + 已装配法宝数（装配越多回复越快，受灵耗制衡）。
	 */
	public static void onEndTick(MinecraftServer server) {
		if (server.getTicks() % 20 != 0) {
			return;
		}
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			CultivationData data = get(player);
			int equipped = equippedCount(player);
			int spirit = Math.min(MAX_SPIRIT, data.spirit() + 2 + equipped);
			if (spirit != data.spirit()) {
				data = data.withSpirit(spirit);
				set(player, data);
			}
			player.sendMessage(Text.translatable("fabao.hud", spirit, MAX_SPIRIT, equipped), true);
		}
	}
}
