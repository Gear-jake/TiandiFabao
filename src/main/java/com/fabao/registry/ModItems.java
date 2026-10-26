package com.fabao.registry;

import com.fabao.FabaoMod;
import com.fabao.item.FabaoItem;
import com.fabao.item.FabaoType;
import com.fabao.item.GuideBook;
import com.fabao.item.YaoDanItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModItems {
	private ModItems() {
	}

	public static RegistryKey<Item> key(String path) {
		return RegistryKey.of(RegistryKeys.ITEM, FabaoMod.id(path));
	}

	private static FabaoItem fabao(FabaoType type) {
		Item.Settings settings = new Item.Settings().registryKey(key(type.id())).maxCount(1);
		if (type.isWeapon()) {
			settings = settings.sword(ToolMaterial.NETHERITE, type.weaponDamage(), type.weaponSpeed());
		}
		return new FabaoItem(type, settings);
	}

	private static Item simple(String path) {
		return new Item(new Item.Settings().registryKey(key(path)));
	}

	// 法宝
	public static final FabaoItem QIANKUN_QUAN = fabao(FabaoType.QIANKUN_QUAN);
	public static final FabaoItem HUNTIAN_LING = fabao(FabaoType.HUNTIAN_LING);
	public static final FabaoItem RUYI_JINGU_BANG = fabao(FabaoType.RUYI_JINGU_BANG);
	public static final FabaoItem DASHEN_BIAN = fabao(FabaoType.DASHEN_BIAN);
	public static final FabaoItem ZIJIN_HULU = fabao(FabaoType.ZIJIN_HULU);
	public static final FabaoItem JINGANG_ZHUO = fabao(FabaoType.JINGANG_ZHUO);
	public static final FabaoItem ZHANYAO_DAO = fabao(FabaoType.ZHANYAO_DAO);
	public static final FabaoItem JIANGYAO_CHU = fabao(FabaoType.JIANGYAO_CHU);
	public static final FabaoItem DINGHAI_SHENZHEN = fabao(FabaoType.DINGHAI_SHENZHEN);

	public static final FabaoItem[] FABAO_ITEMS = {
			QIANKUN_QUAN, HUNTIAN_LING, RUYI_JINGU_BANG, DASHEN_BIAN, ZIJIN_HULU, JINGANG_ZHUO,
			ZHANYAO_DAO, JIANGYAO_CHU, DINGHAI_SHENZHEN
	};

	// 材料
	public static final Item LINGSHI = simple("lingshi");
	public static final Item LINGCAO = simple("lingcao");

	// 妖丹：右键召唤对应等级妖，同时是法宝合成材料
	public static final Item XIAOYAO_DAN = new YaoDanItem(1, new Item.Settings()
			.registryKey(key("xiaoyao_dan")).maxCount(16));
	public static final Item DAYAO_DAN = new YaoDanItem(2, new Item.Settings()
			.registryKey(key("dayao_dan")).maxCount(16));
	public static final Item YAOWANG_DAN = new YaoDanItem(3, new Item.Settings()
			.registryKey(key("yaowang_dan")).maxCount(16));

	// 玩法指南（成书形式，首次进服自动发放）
	public static final Item GUIDE_BOOK = new WrittenBookItem(new Item.Settings()
			.registryKey(key("fabao_book"))
			.maxCount(1)
			.component(DataComponentTypes.WRITTEN_BOOK_CONTENT, GuideBook.content()));

	public static void register() {
		for (FabaoItem item : FABAO_ITEMS) {
			Registry.register(Registries.ITEM, key(item.fabaoType().id()), item);
		}
		Registry.register(Registries.ITEM, key("lingshi"), LINGSHI);
		Registry.register(Registries.ITEM, key("lingcao"), LINGCAO);
		Registry.register(Registries.ITEM, key("xiaoyao_dan"), XIAOYAO_DAN);
		Registry.register(Registries.ITEM, key("dayao_dan"), DAYAO_DAN);
		Registry.register(Registries.ITEM, key("yaowang_dan"), YAOWANG_DAN);
		Registry.register(Registries.ITEM, key("fabao_book"), GUIDE_BOOK);
	}
}
