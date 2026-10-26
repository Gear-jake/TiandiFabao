package com.fabao.loot;

import com.fabao.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;

/**
 * 法宝获取（DESIGN §S2），全部走 LootTableEvents.MODIFY 追加池，不覆盖原版表：
 * 妖王必掉专属法宝 —— 末影龙→金箍棒、凋灵→打神鞭、监守者→金刚琢；
 * 洞天寻宝 —— 沙漠神殿/丛林神庙/地牢/要塞/远古城市宝箱概率出法宝 + 灵石伴生。
 */
public final class ModLootInjection {
	private ModLootInjection() {
	}

	public static void init() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()) {
				return;
			}
			// 妖王必掉
			if (isEntityTable(key, EntityType.ENDER_DRAGON)) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0f))
						.with(ItemEntry.builder(ModItems.RUYI_JINGU_BANG)));
			} else if (isEntityTable(key, EntityType.WITHER)) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0f))
						.with(ItemEntry.builder(ModItems.DASHEN_BIAN)));
			} else if (isEntityTable(key, EntityType.WARDEN)) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0f))
						.with(ItemEntry.builder(ModItems.JINGANG_ZHUO)));
			}
			// 洞天寻宝
			boolean highTier = key.equals(LootTables.ANCIENT_CITY_CHEST);
			if (key.equals(LootTables.DESERT_PYRAMID_CHEST)
					|| key.equals(LootTables.JUNGLE_TEMPLE_CHEST)
					|| key.equals(LootTables.SIMPLE_DUNGEON_CHEST)
					|| key.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)
					|| highTier) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0f))
						.conditionally(RandomChanceLootCondition.builder(highTier ? 0.5f : 0.3f))
						.with(ItemEntry.builder(ModItems.QIANKUN_QUAN).weight(5))
						.with(ItemEntry.builder(ModItems.HUNTIAN_LING).weight(4))
						.with(ItemEntry.builder(highTier ? ModItems.ZIJIN_HULU : ModItems.LINGSHI)
								.weight(highTier ? 3 : 6)));
				tableBuilder.pool(LootPool.builder()
						.rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
						.conditionally(RandomChanceLootCondition.builder(0.6f))
						.with(ItemEntry.builder(ModItems.LINGSHI)));
			}
		});
	}

	private static boolean isEntityTable(RegistryKey<LootTable> key, EntityType<?> type) {
		return type.getLootTableKey().map(key::equals).orElse(false);
	}
}
