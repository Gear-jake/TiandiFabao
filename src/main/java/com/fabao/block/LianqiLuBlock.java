package com.fabao.block;

import com.fabao.screen.LianqiLuScreenHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 炼器炉：带方块实体与 GUI 的祭炼台。
 * 右键打开界面，界面内放入法宝/材料后点按钮执行祭炼/喂养/认主/合成。
 */
public class LianqiLuBlock extends BlockWithEntity {
	public static final MapCodec<LianqiLuBlock> CODEC = createCodec(LianqiLuBlock::new);

	public LianqiLuBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LianqiLuBlockEntity(pos, state);
	}

	@Override
	protected ActionResult onUseWithItem(net.minecraft.item.ItemStack stack, BlockState state, World world,
										 BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return openGui(world, player, pos);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return openGui(world, player, pos);
	}

	private ActionResult openGui(World world, PlayerEntity player, BlockPos pos) {
		if (world.isClient()) {
			return ActionResult.SUCCESS;
		}
		if (player instanceof ServerPlayerEntity serverPlayer) {
			serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory<BlockPos>() {
				@Override
				public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
					return pos;
				}

				@Override
				public Text getDisplayName() {
					return Text.translatable("fabao.gui.lianqi_lu");
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
					return new LianqiLuScreenHandler(syncId, inventory, pos);
				}
			});
		}
		return ActionResult.SUCCESS;
	}
}
