package com.fabao.equipment;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 蓄力状态：PRESS 时记下游戏 tick，RELEASE 时换算蓄力比。
 * 服务端计时，客户端按键丢包时按最低档处理，不会产生超档伤害。
 */
public final class ChargeLogic {
	public static final int FULL_CHARGE_TICKS = 60;
	private static final Map<UUID, Integer> PRESS_TICK = new HashMap<>();

	private ChargeLogic() {
	}

	public static void onPress(ServerPlayerEntity player) {
		PRESS_TICK.put(player.getUuid(), player.getEntityWorld().getServer().getTicks());
	}

	/** 取消蓄力（轮盘按 Esc 取消时清掉 PRESS 记录，避免下次点按变成满蓄）。 */
	public static void cancel(ServerPlayerEntity player) {
		PRESS_TICK.remove(player.getUuid());
	}

	/** 消耗蓄力记录，返回 [0.1, 1.0]；无 PRESS 记录（点按快放/丢包）按 0.25 档。 */
	public static float consumeChargeRatio(ServerPlayerEntity player) {
		Integer start = PRESS_TICK.remove(player.getUuid());
		if (start == null) {
			return 0.25f;
		}
		int held = player.getEntityWorld().getServer().getTicks() - start;
		return Math.clamp(held / (float) FULL_CHARGE_TICKS, 0.1f, 1.0f);
	}
}
