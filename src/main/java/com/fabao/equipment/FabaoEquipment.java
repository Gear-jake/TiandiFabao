package com.fabao.equipment;

import com.fabao.item.FabaoItem;
import com.fabao.registry.ModAttachments;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 「检测」而非「写入」：法宝装备状态完全由 Accessories 饰品框架管理
 * （右键装备 / 饰品界面拖放），这里只做只读查询，供主动技与 HUD 使用。
 */
public final class FabaoEquipment {
	private FabaoEquipment() {
	}

	public static FabaoSlots getState(LivingEntity entity) {
		return entity.getAttachedOrCreate(ModAttachments.SLOTS);
	}

	public static void setState(ServerPlayerEntity player, FabaoSlots state) {
		player.setAttached(ModAttachments.SLOTS, state);
	}

	/** 已装配法宝（Accessories 查询结果，按槽位下标排序）。 */
	public static List<SlotEntryReference> equipped(LivingEntity entity) {
		AccessoriesCapability capability = AccessoriesCapability.get(entity);
		if (capability == null) {
			return List.of();
		}
		List<SlotEntryReference> list = new ArrayList<>(
				capability.getEquipped(stack -> stack.getItem() instanceof FabaoItem));
		list.sort(Comparator.comparingInt(ref -> ref.reference().slot()));
		return list;
	}

	public static int equippedCount(LivingEntity entity) {
		return equipped(entity).size();
	}

	public static ItemStack selectedStack(LivingEntity entity) {
		List<SlotEntryReference> list = equipped(entity);
		if (list.isEmpty()) {
			return ItemStack.EMPTY;
		}
		int index = Math.clamp(getState(entity).selected(), 0, list.size() - 1);
		return list.get(index).stack();
	}

	public static void selectNext(ServerPlayerEntity player) {
		List<SlotEntryReference> list = equipped(player);
		if (list.isEmpty()) {
			player.sendMessage(Text.translatable("fabao.slot.empty_selected"), true);
			return;
		}
		int next = (getState(player).selected() + 1) % list.size();
		setState(player, new FabaoSlots(next));
		ItemStack selected = list.get(next).stack();
		player.sendMessage(Text.translatable("fabao.slot.selected", selected.getName()), true);
	}

	/** fabao 饰品栏容器（槽名 fabao）；找不到返回 null。 */
	public static AccessoriesContainer fabaoContainer(LivingEntity entity) {
		AccessoriesCapability capability = AccessoriesCapability.get(entity);
		if (capability == null) {
			return null;
		}
		for (var entry : capability.getContainers().entrySet()) {
			if (entry.getKey().contains("fabao")) {
				return entry.getValue();
			}
		}
		return null;
	}
}
