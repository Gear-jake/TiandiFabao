package com.fabao.registry;

import com.fabao.FabaoMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public final class ModItemGroups {
	private ModItemGroups() {
	}

	public static final RegistryKey<ItemGroup> MAIN = RegistryKey.of(RegistryKeys.ITEM_GROUP, FabaoMod.id("main"));

	public static void register() {
		Registry.register(Registries.ITEM_GROUP, MAIN, FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup.fabao.main"))
				.icon(() -> new ItemStack(ModItems.QIANKUN_QUAN))
				.entries((context, entries) -> {
					for (Item item : ModItems.FABAO_ITEMS) {
						entries.add(item);
					}
					entries.add(ModItems.LINGSHI);
					entries.add(ModItems.LINGCAO);
					entries.add(ModItems.XIAOYAO_DAN);
					entries.add(ModItems.DAYAO_DAN);
					entries.add(ModItems.YAOWANG_DAN);
					entries.add(ModBlocks.LIANQI_LU);
					entries.add(ModItems.GUIDE_BOOK);
					entries.add(ModDemonSpawns.JINJIAO_SPAWN_EGG);
					entries.add(ModDemonSpawns.YINJIAO_SPAWN_EGG);
				})
				.build());
	}
}
