package com.fabao.registry;

import com.fabao.FabaoMod;
import com.fabao.block.LianqiLuBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

public final class ModBlocks {
	private ModBlocks() {
	}

	public static RegistryKey<Block> blockKey(String path) {
		return RegistryKey.of(RegistryKeys.BLOCK, FabaoMod.id(path));
	}

	public static final Block LIANQI_LU = new LianqiLuBlock(AbstractBlock.Settings.create()
			.registryKey(blockKey("lianqi_lu"))
			.strength(3.5f)
			.requiresTool()
			.sounds(BlockSoundGroup.METAL));

	public static void register() {
		Registry.register(Registries.BLOCK, blockKey("lianqi_lu"), LIANQI_LU);
		Registry.register(Registries.ITEM, ModItems.key("lianqi_lu"),
				new BlockItem(LIANQI_LU, new Item.Settings().registryKey(ModItems.key("lianqi_lu"))));
	}
}
