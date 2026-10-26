package com.fabao.screen;

import com.fabao.block.LianqiLuBlockEntity;
import com.fabao.item.FabaoItem;
import com.fabao.item.FabaoStats;
import com.fabao.registry.ModBlocks;
import com.fabao.registry.ModComponents;
import com.fabao.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * 炼器炉 GUI：2 个操作槽（法宝/材料）+ 玩家背包。
 * 四个按钮（祭炼/喂养/认主/合成）由客户端点击 → clickButton → onButtonClick 在服务端执行；
 * 成功/失败都会在聊天栏（GUI 打开时 actionbar 不可见）给提示并播放音效。
 */
public class LianqiLuScreenHandler extends ScreenHandler {
	private record ForgeRecipe(Item main, int mainCount, int stoneCount, ItemStack result) {
	}

	private static final List<ForgeRecipe> RECIPES = List.of(
			new ForgeRecipe(Items.GOLD_INGOT, 4, 4, new ItemStack(ModItems.QIANKUN_QUAN)),
			new ForgeRecipe(Items.RED_WOOL, 4, 4, new ItemStack(ModItems.HUNTIAN_LING)),
			new ForgeRecipe(Items.AMETHYST_SHARD, 4, 8, new ItemStack(ModItems.ZIJIN_HULU)));

	private final Inventory inventory;
	private final BlockPos pos;
	private final World world;

	public LianqiLuScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
		super(ModScreenHandlers.LIANQI_LU, syncId);
		this.pos = pos;
		this.world = playerInventory.player.getEntityWorld();
		this.inventory = world.getBlockEntity(pos) instanceof LianqiLuBlockEntity be
				? be.inventory()
				: new SimpleInventory(2);

		this.addSlot(new Slot(inventory, LianqiLuBlockEntity.SLOT_FABAO, 56, 22));
		this.addSlot(new Slot(inventory, LianqiLuBlockEntity.SLOT_MATERIAL, 56, 50));

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return ScreenHandler.canUse(ScreenHandlerContext.create(world, pos), player, ModBlocks.LIANQI_LU);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		Slot slot = this.slots.get(index);
		if (!slot.hasStack()) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = slot.getStack();
		ItemStack original = stack.copy();
		if (index < 2) {
			if (!this.insertItem(stack, 2, this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.insertItem(stack, 0, 2, false)) {
				return ItemStack.EMPTY;
			}
		}
		if (stack.isEmpty()) {
			slot.setStack(ItemStack.EMPTY);
		} else {
			slot.markDirty();
		}
		return original;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (!(player instanceof ServerPlayerEntity serverPlayer)
				|| !(world.getBlockEntity(pos) instanceof LianqiLuBlockEntity be)) {
			return false;
		}
		ItemStack subject = be.inventory().getStack(LianqiLuBlockEntity.SLOT_FABAO);
		ItemStack material = be.inventory().getStack(LianqiLuBlockEntity.SLOT_MATERIAL);
		boolean ok = switch (id) {
			case 0 -> temper(serverPlayer, be, subject, material);
			case 1 -> feed(serverPlayer, be, subject, material);
			case 2 -> bind(serverPlayer, be, subject);
			case 3 -> craft(serverPlayer, be, subject, material);
			default -> false;
		};
		world.playSound(null, pos, ok ? SoundEvents.BLOCK_BELL_USE : SoundEvents.ENTITY_VILLAGER_NO,
				SoundCategory.BLOCKS, 1.0f, ok ? 1.2f : 0.9f);
		be.markDirty();
		return true;
	}

	/** 九转祭炼：灵石升阶，每阶 +10% 威力。 */
	private boolean temper(ServerPlayerEntity player, LianqiLuBlockEntity be, ItemStack subject, ItemStack material) {
		if (!(subject.getItem() instanceof FabaoItem)) {
			player.sendMessage(Text.translatable("fabao.gui.need_fabao"), false);
			return false;
		}
		int tier = FabaoStats.tier(subject);
		if (tier >= FabaoStats.MAX_TIER) {
			player.sendMessage(Text.translatable("fabao.forge.tier_max", subject.getName()), false);
			return false;
		}
		int cost = (tier + 1) * 2;
		if (!material.isOf(ModItems.LINGSHI) || material.getCount() < cost) {
			player.sendMessage(Text.translatable("fabao.forge.need_stone", cost), false);
			return false;
		}
		material.decrement(cost);
		consumeIfEmpty(be, material, LianqiLuBlockEntity.SLOT_MATERIAL);
		FabaoStats.setTier(subject, tier + 1);
		player.sendMessage(Text.translatable("fabao.forge.tier_up", subject.getName(), tier + 1), false);
		return true;
	}

	/** 灵材喂养：灵草换随机威能词条。 */
	private boolean feed(ServerPlayerEntity player, LianqiLuBlockEntity be, ItemStack subject, ItemStack material) {
		if (!(subject.getItem() instanceof FabaoItem)) {
			player.sendMessage(Text.translatable("fabao.gui.need_fabao"), false);
			return false;
		}
		if (!material.isOf(ModItems.LINGCAO)) {
			player.sendMessage(Text.translatable("fabao.gui.need_lingcao"), false);
			return false;
		}
		material.decrement(1);
		consumeIfEmpty(be, material, LianqiLuBlockEntity.SLOT_MATERIAL);
		int roll = player.getRandom().nextInt(100);
		int bonus = roll < 40 ? 5 : roll < 70 ? 8 : roll < 90 ? 12 : 15;
		subject.set(ModComponents.AFFIX, bonus);
		player.sendMessage(Text.translatable("fabao.forge.feed", subject.getName(), bonus), false);
		return true;
	}

	/** 血脉认主：+20% 威力，他人不可装配。 */
	private boolean bind(ServerPlayerEntity player, LianqiLuBlockEntity be, ItemStack subject) {
		if (!(subject.getItem() instanceof FabaoItem)) {
			player.sendMessage(Text.translatable("fabao.gui.need_fabao"), false);
			return false;
		}
		if (FabaoStats.isBound(subject)) {
			player.sendMessage(Text.translatable("fabao.forge.bound"), false);
			return false;
		}
		FabaoStats.bind(subject, player);
		player.sendMessage(Text.translatable("fabao.forge.bind", subject.getName()), false);
		return true;
	}

	/** 炼器合成：主材料 + 灵石合成法宝。 */
	private boolean craft(ServerPlayerEntity player, LianqiLuBlockEntity be, ItemStack subject, ItemStack material) {
		for (ForgeRecipe recipe : RECIPES) {
			if (subject.isOf(recipe.main()) && subject.getCount() >= recipe.mainCount()
					&& material.isOf(ModItems.LINGSHI) && material.getCount() >= recipe.stoneCount()) {
				subject.decrement(recipe.mainCount());
				material.decrement(recipe.stoneCount());
				consumeIfEmpty(be, subject, LianqiLuBlockEntity.SLOT_FABAO);
				consumeIfEmpty(be, material, LianqiLuBlockEntity.SLOT_MATERIAL);
				player.getInventory().offerOrDrop(recipe.result().copy());
				player.sendMessage(Text.translatable("fabao.forge.craft", recipe.result().getName()), false);
				return true;
			}
		}
		player.sendMessage(Text.translatable("fabao.forge.no_recipe"), false);
		return false;
	}

	private void consumeIfEmpty(LianqiLuBlockEntity be, ItemStack stack, int slot) {
		if (stack.isEmpty()) {
			be.inventory().setStack(slot, ItemStack.EMPTY);
		}
	}
}
