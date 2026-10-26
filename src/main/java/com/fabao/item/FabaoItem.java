package com.fabao.item;

import com.fabao.equipment.FabaoActivator;
import com.fabao.registry.ModComponents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Consumer;

/**
 * 法宝载体物品。两类用法：
 * - 法器型（乾坤圈/混天绫/打神鞭/紫金葫芦/金刚琢）：饰品，交给 Accessories 框架装备，轮盘发动；
 * - 兵器型（金箍棒/斩妖刀/降妖杵/定海神针）：手持武器，左键挥砍 + 右键按住蓄力、松开释放技能。
 */
public class FabaoItem extends Item {
	public static final int CHARGE_TICKS = 60;

	private final FabaoType type;

	public FabaoItem(FabaoType type, Settings settings) {
		super(settings);
		this.type = type;
	}

	public FabaoType fabaoType() {
		return type;
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (!type.isWeapon()) {
			// 法器：交给 Accessories 的快速装备（canEquipFromUse）处理
			return ActionResult.PASS;
		}
		user.setCurrentHand(hand);
		return ActionResult.CONSUME;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return type.isWeapon() ? CHARGE_TICKS : 0;
	}

	@Override
	public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (!type.isWeapon() || !(user instanceof ServerPlayerEntity player)) {
			return false;
		}
		float ratio = MathHelper.clamp((CHARGE_TICKS - remainingUseTicks) / (float) CHARGE_TICKS, 0.1f, 1.0f);
		FabaoActivator.releaseHeld(player, this, ratio);
		return true;
	}

	/** 物品提示：灵耗 / 对妖 / 品阶 / 词条 / 认主 / 用法。 */
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent,
							  Consumer<Text> tooltip, TooltipType tooltipType) {
		super.appendTooltip(stack, context, displayComponent, tooltip, tooltipType);
		tooltip.accept(Text.translatable(type.isWeapon() ? "fabao.tooltip.weapon" : "fabao.tooltip.accessory")
				.formatted(Formatting.DARK_GRAY));
		tooltip.accept(Text.translatable("fabao.tooltip.spirit", type.spiritCost()).formatted(Formatting.AQUA));
		if (type.isYaoSlayer()) {
			tooltip.accept(Text.translatable("fabao.tooltip.yao_slayer",
					Math.round(type.yaoBonus() * 100)).formatted(Formatting.DARK_PURPLE));
		}
		int tier = FabaoStats.tier(stack);
		if (tier > 0) {
			tooltip.accept(Text.translatable("fabao.tooltip.tier", tier).formatted(Formatting.GOLD));
		}
		int affix = FabaoStats.affix(stack);
		if (affix > 0) {
			tooltip.accept(Text.translatable("fabao.tooltip.affix", affix).formatted(Formatting.YELLOW));
		}
		if (FabaoStats.isBound(stack)) {
			tooltip.accept(Text.translatable("fabao.tooltip.bound",
					stack.getOrDefault(ModComponents.OWNER_NAME, "?")).formatted(Formatting.RED));
		}
	}
}
