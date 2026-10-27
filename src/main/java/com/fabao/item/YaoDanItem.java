package com.fabao.item;

import com.fabao.yao.YaoManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * 妖丹：右键在身前召唤对应等级的妖（1 小妖 / 2 大妖 / 3 妖王）。
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
		if (!world.isClient() && user instanceof ServerPlayerEntity player) {
			// 潜行右键 = 按顺序循环召（狐妖→蜘蛛精→黑熊精），普通右键 = 随机
			if (player.isSneaking()) {
				YaoManager.summonCycle(player, tier);
			} else {
				YaoManager.summon(player, tier);
			}
			user.getStackInHand(hand).decrement(1);
		}
		return ActionResult.SUCCESS;
	}
}
