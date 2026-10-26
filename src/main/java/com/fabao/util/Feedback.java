package com.fabao.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * 统一反馈：成功走聊天栏（白色），失败走聊天栏（红色）+ 村民摇头音。
 * GUI 或战斗中 actionbar 容易忽略，所以提示一律用聊天栏。
 */
public final class Feedback {
	private Feedback() {
	}

	public static void success(ServerPlayerEntity player, String key, Object... args) {
		player.sendMessage(Text.translatable(key, args), false);
	}

	public static void fail(ServerPlayerEntity player, String key, Object... args) {
		player.sendMessage(Text.translatable(key, args).formatted(Formatting.RED), false);
		player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
				SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 0.5f, 1.1f);
	}

	public static void bar(ServerPlayerEntity player, Text text) {
		player.sendMessage(text, true);
	}
}
