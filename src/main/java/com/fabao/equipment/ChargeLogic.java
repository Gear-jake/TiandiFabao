package com.fabao.equipment;

import com.fabao.util.ParticleFx;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 蓄力状态：PRESS 时记下游戏 tick，RELEASE 时换算蓄力比。
 * 服务端计时，客户端按键丢包时按最低档处理，不会产生超档伤害。
 * 蓄力期间每 3 tick 出一圈螺旋光点（视觉反馈蓄力进度）。
 */
public final class ChargeLogic {
	public static final int FULL_CHARGE_TICKS = 60;
	private static final Map<UUID, Integer> PRESS_TICK = new HashMap<>();
	/** 当前正在蓄力的玩家（用于持续播放粒子）。 */
	private static final Set<UUID> CHARGING = new HashSet<>();

	private ChargeLogic() {
	}

	public static void onPress(ServerPlayerEntity player) {
		PRESS_TICK.put(player.getUuid(), player.getEntityWorld().getServer().getTicks());
		CHARGING.add(player.getUuid());
	}

	/** 取消蓄力（轮盘按 Esc 取消时清掉 PRESS 记录，避免下次点按变成满蓄）。 */
	public static void cancel(ServerPlayerEntity player) {
		PRESS_TICK.remove(player.getUuid());
		CHARGING.remove(player.getUuid());
	}

	/** 消耗蓄力记录，返回 [0.1, 1.0]；无 PRESS 记录（点按快放/丢包）按 0.25 档。 */
	public static float consumeChargeRatio(ServerPlayerEntity player) {
		Integer start = PRESS_TICK.remove(player.getUuid());
		CHARGING.remove(player.getUuid());
		if (start == null) {
			return 0.25f;
		}
		int held = player.getEntityWorld().getServer().getTicks() - start;
		return Math.clamp(held / (float) FULL_CHARGE_TICKS, 0.1f, 1.0f);
	}

	/** 服务端每 tick 调用：给蓄力中的玩家画螺旋光点 + 随进度变亮。 */
	public static void tickParticles(MinecraftServer server) {
		if (CHARGING.isEmpty()) {
			return;
		}
		int ticks = server.getTicks();
		if (ticks % 3 != 0) {
			return;
		}
		// 清理已下线/已释放的玩家
		CHARGING.removeIf(uuid -> server.getPlayerManager().getPlayer(uuid) == null);
		for (UUID uuid : CHARGING) {
			ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
			if (player == null) {
				continue;
			}
			Integer start = PRESS_TICK.get(uuid);
			float progress = start == null ? 0.2f
					: Math.clamp((ticks - start) / (float) FULL_CHARGE_TICKS, 0.1f, 1.0f);
			int color = progress >= 1.0f ? ParticleFx.GOLD
					: progress > 0.6f ? ParticleFx.ORANGE : ParticleFx.CYAN;
			ParticleFx.chargeSpiral((ServerWorld) player.getEntityWorld(), player, color, progress);
		}
	}

	/** 是否在蓄力（供其他地方判断）。 */
	public static boolean isCharging(ServerPlayerEntity player) {
		return CHARGING.contains(player.getUuid());
	}
}
