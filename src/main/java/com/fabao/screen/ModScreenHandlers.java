package com.fabao.screen;

import com.fabao.FabaoMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;

public final class ModScreenHandlers {
	public static final ExtendedScreenHandlerType<LianqiLuScreenHandler, BlockPos> LIANQI_LU =
			new ExtendedScreenHandlerType<>(
					(syncId, inventory, pos) -> new LianqiLuScreenHandler(syncId, inventory, pos),
					BlockPos.PACKET_CODEC);

	private ModScreenHandlers() {
	}

	public static void init() {
		Registry.register(Registries.SCREEN_HANDLER, FabaoMod.id("lianqi_lu"), LIANQI_LU);
	}
}
