package com.fabao.item;

import com.fabao.cultivation.CultivationManager;
import com.fabao.util.Feedback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * 妖丹：修炼资源。右键炼化，按品阶补充灵力
 * （小妖丹 +20 / 大妖丹 +40 / 妖王丹 +60）。
 * 妖丹同时是炼器/合成法宝的材料（见工作台配方）。
 */
public class YaoDanItem extends Item {
	private final int tier;

	public YaoDanItem(int tier, Settings settings) {
		super(settings);
		this.tier = tier;
	}

	public int tier() {
		return tier;
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient() || !(user instanceof ServerPlayerEntity player)) {
			return ActionResult.SUCCESS;
		}
		int want = switch (tier) {
			case 1 -> 20;
			case 2 -> 40;
			default -> 60;
		};
		int gained = CultivationManager.addSpirit(player, want);
		if (gained <= 0) {
			Feedback.fail(player, "fabao.dan.full");
			return ActionResult.FAIL;
		}
		Feedback.success(player, "fabao.dan.gain", gained);
		user.getStackInHand(hand).decrement(1);
		return ActionResult.SUCCESS;
	}
}
