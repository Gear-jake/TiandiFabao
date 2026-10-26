package com.fabao.block;

import com.fabao.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

/**
 * 炼器炉方块实体：2 槽（法宝/材料），GUI 内操作祭炼/喂养/认主/合成。
 * 存储走 1.21.10 的 ReadView/WriteView API。
 */
public class LianqiLuBlockEntity extends BlockEntity {
	public static final int SLOT_FABAO = 0;
	public static final int SLOT_MATERIAL = 1;

	private final SimpleInventory inventory = new SimpleInventory(2);

	public LianqiLuBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.LIANQI_LU, pos, state);
	}

	public SimpleInventory inventory() {
		return inventory;
	}

	@Override
	protected void writeData(WriteView view) {
		view.put("fabao_slot", ItemStack.OPTIONAL_CODEC, inventory.getStack(SLOT_FABAO));
		view.put("material_slot", ItemStack.OPTIONAL_CODEC, inventory.getStack(SLOT_MATERIAL));
	}

	@Override
	protected void readData(ReadView view) {
		inventory.setStack(SLOT_FABAO,
				view.read("fabao_slot", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
		inventory.setStack(SLOT_MATERIAL,
				view.read("material_slot", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
	}
}
