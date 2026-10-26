package com.fabao.equipment;

import com.fabao.FabaoMod;
import com.fabao.net.FabaoKeyPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * 按键意图分派。装备动作由 Accessories 框架负责（右键装备 / 饰品界面），
 * 这里只处理蓄力、释放与选中槽切换；G/V 给出操作提示。
 */
public final class FabaoKeyHandler {
	private FabaoKeyHandler() {
	}

	public static void receive(FabaoKeyPayload payload, ServerPlayNetworking.Context context) {
		ServerPlayerEntity player = context.player();
		FabaoMod.LOGGER.info("[Fabao] key {} from {}", payload.action(), player.getName().getString());
		switch (payload.action()) {
			case PRESS -> ChargeLogic.onPress(player);
			case RELEASE -> FabaoActivator.release(player, payload.slot());
			case SELECT_NEXT -> FabaoEquipment.selectNext(player);
			case EQUIP -> player.sendMessage(Text.translatable("fabao.tip.equip"), false);
			case UNEQUIP -> player.sendMessage(Text.translatable("fabao.tip.unequip"), false);
			case CANCEL -> ChargeLogic.cancel(player);
		}
	}
}
