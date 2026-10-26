package com.fabao.registry;

import com.fabao.FabaoMod;
import com.fabao.block.LianqiLuBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModBlockEntities {
	public static final BlockEntityType<LianqiLuBlockEntity> LIANQI_LU = Registry.register(
			Registries.BLOCK_ENTITY_TYPE, FabaoMod.id("lianqi_lu"),
			FabricBlockEntityTypeBuilder.create(LianqiLuBlockEntity::new, ModBlocks.LIANQI_LU).build());

	private ModBlockEntities() {
	}

	public static void init() {
	}
}
