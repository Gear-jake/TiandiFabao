package com.fabao.net;

import com.fabao.FabaoMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * 客户端按键意图 → 服务端。
 * RELEASE 带 slot（-1 = 当前选中；轮盘选择时传具体槽位）。
 */
public record FabaoKeyPayload(Action action, int slot) implements CustomPayload {
	public enum Action {
		PRESS, RELEASE, SELECT_NEXT, EQUIP, UNEQUIP, CANCEL;

		public static Action byOrdinal(int ordinal) {
			Action[] all = values();
			return all[Math.floorMod(ordinal, all.length)];
		}
	}

	public static final Id<FabaoKeyPayload> ID = new Id<>(FabaoMod.id("fabao_key"));

	public static final PacketCodec<RegistryByteBuf, FabaoKeyPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, payload -> payload.action().ordinal(),
			PacketCodecs.VAR_INT, FabaoKeyPayload::slot,
			(ordinal, slot) -> new FabaoKeyPayload(Action.byOrdinal(ordinal), slot));

	public FabaoKeyPayload(Action action) {
		this(action, -1);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
