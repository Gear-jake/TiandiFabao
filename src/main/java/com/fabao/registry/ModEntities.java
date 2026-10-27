package com.fabao.registry;

import com.fabao.FabaoMod;
import com.fabao.entity.BearDemonEntity;
import com.fabao.entity.FoxDemonEntity;
import com.fabao.entity.SpiderDemonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

/**
 * 自定义妖物实体注册（替换原版模板，使用自制模型）。
 */
public final class ModEntities {
	public static final RegistryKey<EntityType<?>> FOX_DEMON_KEY =
			RegistryKey.of(RegistryKeys.ENTITY_TYPE, FabaoMod.id("fox_demon"));
	public static final RegistryKey<EntityType<?>> SPIDER_DEMON_KEY =
			RegistryKey.of(RegistryKeys.ENTITY_TYPE, FabaoMod.id("spider_demon"));
	public static final RegistryKey<EntityType<?>> BEAR_DEMON_KEY =
			RegistryKey.of(RegistryKeys.ENTITY_TYPE, FabaoMod.id("bear_demon"));

	public static final EntityType<FoxDemonEntity> FOX_DEMON = Registry.register(
			Registries.ENTITY_TYPE, FOX_DEMON_KEY,
			EntityType.Builder.create(FoxDemonEntity::new, SpawnGroup.CREATURE)
					.dimensions(0.7f, 1.75f).maxTrackingRange(10).build(FOX_DEMON_KEY));

	public static final EntityType<SpiderDemonEntity> SPIDER_DEMON = Registry.register(
			Registries.ENTITY_TYPE, SPIDER_DEMON_KEY,
			EntityType.Builder.create(SpiderDemonEntity::new, SpawnGroup.CREATURE)
					.dimensions(1.2f, 0.7f).maxTrackingRange(10).build(SPIDER_DEMON_KEY));

	public static final EntityType<BearDemonEntity> BEAR_DEMON = Registry.register(
			Registries.ENTITY_TYPE, BEAR_DEMON_KEY,
			EntityType.Builder.create(BearDemonEntity::new, SpawnGroup.CREATURE)
					.dimensions(1.1f, 1.9f).maxTrackingRange(10).build(BEAR_DEMON_KEY));

	private ModEntities() {
	}

	public static void init() {
		FabricDefaultAttributeRegistry.register(FOX_DEMON, FoxDemonEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(SPIDER_DEMON, SpiderDemonEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(BEAR_DEMON, BearDemonEntity.createAttributes());
	}
}
