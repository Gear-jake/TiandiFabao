package com.fabao.registry;

import com.fabao.FabaoMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * 金角/银角大王的刷怪蛋 + 自然生成。
 * 1.21.10 的 SpawnEggItem 单参数构造，实体类型走 ENTITY_DATA 组件。
 */
public final class ModDemonSpawns {
	public static final Item JINJIAO_SPAWN_EGG = egg(ModEntities.JINJIAO_DAWANG, "jinjiao_dawang_spawn_egg");
	public static final Item YINJIAO_SPAWN_EGG = egg(ModEntities.YINJIAO_DAWANG, "yinjiao_dawang_spawn_egg");
	public static final Item BAIGUJING_SPAWN_EGG = egg(ModEntities.BAIGUJING, "baigujing_spawn_egg");

	private ModDemonSpawns() {
	}

	private static Item egg(EntityType<?> type, String id) {
		Item.Settings settings = new Item.Settings()
				.registryKey(net.minecraft.registry.RegistryKey.of(
						net.minecraft.registry.RegistryKeys.ITEM, FabaoMod.id(id)))
				.component(DataComponentTypes.ENTITY_DATA,
						net.minecraft.entity.TypedEntityData.create(type, new net.minecraft.nbt.NbtCompound()));
		return new SpawnEggItem(settings);
	}

	public static void init() {
		Registry.register(Registries.ITEM, FabaoMod.id("jinjiao_dawang_spawn_egg"), JINJIAO_SPAWN_EGG);
		Registry.register(Registries.ITEM, FabaoMod.id("yinjiao_dawang_spawn_egg"), YINJIAO_SPAWN_EGG);
		Registry.register(Registries.ITEM, FabaoMod.id("baigujing_spawn_egg"), BAIGUJING_SPAWN_EGG);

		BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.MONSTER,
				ModEntities.JINJIAO_DAWANG, 2, 1, 1);
		BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.MONSTER,
				ModEntities.YINJIAO_DAWANG, 2, 1, 1);
		BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.MONSTER,
				ModEntities.BAIGUJING, 2, 1, 1);
	}
}
