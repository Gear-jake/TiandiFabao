package com.fabao.cultivation;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * 玩家灵力数据（Attachment，不可变 record）。境界机制已移除（P10），只保留灵力资源池。
 */
public record CultivationData(int spirit) {
	public static final CultivationData DEFAULT = new CultivationData(100);

	public static final Codec<CultivationData> CODEC =
			Codec.INT.xmap(CultivationData::new, CultivationData::spirit);

	public static final PacketCodec<RegistryByteBuf, CultivationData> PACKET_CODEC =
			PacketCodecs.VAR_INT.xmap(CultivationData::new, CultivationData::spirit).cast();

	public CultivationData withSpirit(int newSpirit) {
		return new CultivationData(newSpirit);
	}
}
