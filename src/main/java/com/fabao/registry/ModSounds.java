package com.fabao.registry;

import com.fabao.FabaoMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * 自定义音效事件。sounds.json 引用原版音频文件（路径经 1.21.10 assetIndex 核实）：
 * 钟磬 = block/bell/bell_use01+02；诵经 = ambient/nether/soulsand_valley/voices1+2。
 * 雷法轰鸣直接用原版 SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER（不重复注册）。
 */
public final class ModSounds {
	public static final SoundEvent ZHONGQING_RELEASE = register("zhongqing_release");
	public static final SoundEvent SONGJING_AMBIENT = register("songjing_ambient");

	private ModSounds() {
	}

	private static SoundEvent register(String path) {
		Identifier id = FabaoMod.id(path);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	public static void init() {
	}
}
