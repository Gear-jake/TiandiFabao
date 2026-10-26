package com.fabao.net;

import com.fabao.equipment.FabaoKeyHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ModPackets {
	private ModPackets() {
	}

	public static void init() {
		PayloadTypeRegistry.playC2S().register(FabaoKeyPayload.ID, FabaoKeyPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(FabaoKeyPayload.ID, FabaoKeyHandler::receive);
	}
}
