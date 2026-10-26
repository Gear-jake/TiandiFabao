package com.fabao.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * 法宝装配状态（Attachment，不可变 record）：只存「当前选中槽」。
 * 槽位内容由 Accessories 饰品栏持有（见 FabaoAccessory），不再自研存储。
 */
public record FabaoSlots(int selected) {
	public static final int SLOT_COUNT = 3;

	public static final Codec<FabaoSlots> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("selected").forGetter(FabaoSlots::selected)
	).apply(instance, FabaoSlots::new));

	public static final PacketCodec<RegistryByteBuf, FabaoSlots> PACKET_CODEC =
			PacketCodecs.VAR_INT.xmap(FabaoSlots::new, FabaoSlots::selected).cast();

	public static FabaoSlots empty() {
		return new FabaoSlots(0);
	}

	public FabaoSlots withSelected(int index) {
		return new FabaoSlots(Math.clamp(index, 0, SLOT_COUNT - 1));
	}
}
