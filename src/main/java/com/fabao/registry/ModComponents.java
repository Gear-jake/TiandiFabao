package com.fabao.registry;

import com.fabao.FabaoMod;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * 法宝数据组件（1.21.10 无物品 NBT）：
 * tier = 九转品阶 0-9；owner_uuid/owner_name = 血脉认主。
 */
public final class ModComponents {
	public static final ComponentType<Integer> TIER = Registry.register(
			Registries.DATA_COMPONENT_TYPE, FabaoMod.id("tier"),
			ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.VAR_INT).build());

	public static final ComponentType<String> OWNER_UUID = Registry.register(
			Registries.DATA_COMPONENT_TYPE, FabaoMod.id("owner_uuid"),
			ComponentType.<String>builder().codec(Codec.STRING).packetCodec(PacketCodecs.STRING).build());

	public static final ComponentType<String> OWNER_NAME = Registry.register(
			Registries.DATA_COMPONENT_TYPE, FabaoMod.id("owner_name"),
			ComponentType.<String>builder().codec(Codec.STRING).packetCodec(PacketCodecs.STRING).build());

	/** 灵材喂养词条：威能加成百分比（5/8/12/15）。 */
	public static final ComponentType<Integer> AFFIX = Registry.register(
			Registries.DATA_COMPONENT_TYPE, FabaoMod.id("affix"),
			ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.VAR_INT).build());

	private ModComponents() {
	}

	public static void init() {
	}
}
