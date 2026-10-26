package com.fabao.item;

import com.fabao.registry.ModItems;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.RawFilteredPair;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * 玩法指南书「封神西游·法宝谱」：成书（WrittenBookItem）形式，右键翻页。
 * 玩家首次进服自动发放（背包已有则不重复发）。
 * 书页底色是米黄羊皮纸，文字统一用深色（黑/深蓝/深灰），不用亮色。
 */
public final class GuideBook {
	private GuideBook() {
	}

	public static WrittenBookContentComponent content() {
		List<RawFilteredPair<Text>> pages = List.of(
				page(title("封神西游·法宝谱")
						.append(Text.literal("\n\n"))
						.append(Text.literal("此录载封神、西游两教法宝之秘与诸妖之数。\n\n")
								.formatted(Formatting.DARK_GRAY))
						.append(Text.literal("右击翻页，自取所需。\n\n")
								.formatted(Formatting.DARK_GRAY))
						.append(Text.literal("—— 仙缘").formatted(Formatting.DARK_GRAY, Formatting.ITALIC))),

				page(title("一、法宝从何而来")
						.append(section("\n\n① 工作台合成"))
						.append(body("以稀有矿物、怪物掉落与妖丹合成，配方见合成书。\n\n"))
						.append(section("② 洞天寻宝"))
						.append(body("沙漠神殿、丛林神庙、地牢、要塞、远古城市的宝箱中或有法宝。\n\n"))
						.append(section("③ 妖王必掉"))
						.append(body("末影龙→金箍棒，凋灵→打神鞭，监守者→金刚琢。"))),

				page(title("二、如何装配")
						.append(section("\n\n法宝是饰品"))
						.append(body("右键法宝，装入饰品栏左侧的「法宝」三槽。\n\n"))
						.append(section("按键"))
						.append(body("R  按住蓄力，松手释放\n"))
						.append(body("F  切换选中的法宝\n"))
						.append(body("G  装备提示\n"))
						.append(body("V  卸下提示\n\n"))
						.append(desc("满蓄（约三秒）伤害翻倍；满蓄时潜行释放，召出器灵护主八息。"))),

				page(title("三、祭炼与品阶")
						.append(desc("\n\n需「炼器炉」（金锭×4、铁锭×4、熔炉合成）。\n\n"))
						.append(section("灵石 + 法宝"))
						.append(body("九转祭炼：每转 +10% 威力\n\n"))
						.append(section("灵草 + 法宝"))
						.append(body("灵材喂养：随机获得 +5%~15% 威能词条\n\n"))
						.append(section("直接点「认主」"))
						.append(body("血脉认主：+20% 威力，他人不可装配\n\n"))
						.append(desc("祭炼后需重新装配，属性方会刷新。"))),

				page(title("四、妖与妖丹")
						.append(section("\n\n右击妖丹，召出对应妖物"))
						.append(body("小妖丹 → 狐妖·魅惑 / 蜘蛛精·织网 / 黑熊精·冲锋\n"))
						.append(body("大妖丹 → 白骨精·吸血 / 金角·葫芦收人 / 银角·金绳束缚\n"))
						.append(body("妖王丹 → 六耳猕猴·幻术 / 金翅大鹏·俯冲\n\n"))
						.append(section("击杀妖物得妖丹"))
						.append(body("妖丹可造克制妖物的法宝：\n"))
						.append(body("小妖丹→斩妖刀，大妖丹→降妖杵，妖王丹→定海神针。"))),

				page(title("五、战斗与连招")
						.append(section("\n\n六件法宝各有原著神通"))
						.append(body("乾坤圈·掷出必中  混天绫·缚仙索\n"))
						.append(body("金箍棒·万钧横扫  打神鞭·专打元神\n"))
						.append(body("紫金红葫芦·喊名收人  金刚琢·套走兵器\n\n"))
						.append(section("连招"))
						.append(body("八秒内依次使出三件不同法宝，触发「三才合璧」，十五格内天雷荡妖。\n\n"))
						.append(section("灵力"))
						.append(body("释放消耗灵力，上限百点，装配法宝越多回灵越快。"))),

				page(title("六、灵材")
						.append(section("\n\n灵石"))
						.append(body("钻石×1 + 青金石×2 → 灵石×2\n\n"))
						.append(section("灵草"))
						.append(body("矮草丛×2 + 骨粉×1 → 灵草×2\n\n"))
						.append(section("妖丹法宝配方"))
						.append(body("斩妖刀：小妖丹×4 + 钻石×2 + 铁锭×2\n"))
						.append(body("降妖杵：大妖丹×4 + 紫水晶×4 + 金块\n"))
						.append(body("定海神针：妖王丹×2 + 下界之星 + 钻石块×2 + 海晶碎片×4"))));

		return new WrittenBookContentComponent(
				RawFilteredPair.of("封神西游·法宝谱"), "仙缘", 0, pages, true);
	}

	private static RawFilteredPair<Text> page(Text text) {
		return RawFilteredPair.of(text);
	}

	/** 页标题：黑色加粗（MutableText 以便链式 append）。 */
	private static MutableText title(String text) {
		return Text.literal(text).formatted(Formatting.BOLD);
	}

	/** 小节标题：深蓝加粗。 */
	private static Text section(String text) {
		return Text.literal(text).formatted(Formatting.DARK_BLUE, Formatting.BOLD);
	}

	/** 正文：默认黑色。 */
	private static Text body(String text) {
		return Text.literal(text);
	}

	/** 说明/备注：深灰。 */
	private static Text desc(String text) {
		return Text.literal(text).formatted(Formatting.DARK_GRAY);
	}

	/** 首次进服发放指南书（背包里已有则不重复发）。 */
	public static void giveTo(ServerPlayerEntity player) {
		ItemStack book = new ItemStack(ModItems.GUIDE_BOOK);
		if (player.getInventory().contains(book)) {
			return;
		}
		player.getInventory().offerOrDrop(book);
	}
}
