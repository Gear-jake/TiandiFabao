package com.fabao;

import com.fabao.combat.QilingManager;
import com.fabao.cultivation.CultivationManager;
import com.fabao.equipment.FabaoAccessory;
import com.fabao.equipment.HeldWeaponPassive;
import com.fabao.item.GuideBook;
import com.fabao.loot.ModLootInjection;
import com.fabao.net.ModPackets;
import com.fabao.registry.ModAttachments;
import com.fabao.registry.ModBlockEntities;
import com.fabao.registry.ModBlocks;
import com.fabao.registry.ModComponents;
import com.fabao.registry.ModEntities;
import com.fabao.registry.ModItemGroups;
import com.fabao.registry.ModItems;
import com.fabao.registry.ModParticles;
import com.fabao.registry.ModSounds;
import com.fabao.screen.ModScreenHandlers;
import com.fabao.yao.YaoManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabaoMod implements ModInitializer {
	public static final String MOD_ID = "fabao";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModAttachments.init();
		ModComponents.init();
		ModPackets.init();
		ModItems.register();
		ModEntities.init();
		com.fabao.registry.ModDemonSpawns.init();
		ModBlocks.register();
		ModBlockEntities.init();
		ModScreenHandlers.init();
		ModItemGroups.register();
		ModLootInjection.init();
		ModParticles.init();
		ModSounds.init();
		FabaoAccessory.init();
		YaoManager.init();
		io.wispforest.accessories.api.events.AccessoryChangeCallback.EVENT.register(
				(prevStack, currentStack, reference, stateChange) -> {
					if (!(reference.entity() instanceof net.minecraft.server.network.ServerPlayerEntity player)) {
						return;
					}
					if (!prevStack.isEmpty() && prevStack.getItem() instanceof com.fabao.item.FabaoItem
							&& currentStack.isEmpty()) {
						com.fabao.util.Feedback.success(player, "fabao.unequip.success", prevStack.getName());
					} else if (prevStack.isEmpty() && currentStack.getItem() instanceof com.fabao.item.FabaoItem) {
						com.fabao.util.Feedback.success(player, "fabao.equip.success", currentStack.getName());
					}
				});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> GuideBook.giveTo(handler.getPlayer()));
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			java.util.UUID id = handler.getPlayer().getUuid();
			com.fabao.equipment.FabaoActivator.clearPlayer(id);
			com.fabao.equipment.ChargeLogic.clearPlayer(id);
			com.fabao.equipment.HeldWeaponPassive.clearPlayer(id);
			com.fabao.combat.ComboTracker.clearPlayer(id);
			com.fabao.combat.QilingManager.clearPlayer(id);
		});
		ServerTickEvents.END_SERVER_TICK.register(CultivationManager::onEndTick);
		ServerTickEvents.END_SERVER_TICK.register(QilingManager::onEndTick);
		ServerTickEvents.END_SERVER_TICK.register(HeldWeaponPassive::onEndTick);
		ServerTickEvents.END_SERVER_TICK.register(com.fabao.equipment.ChargeLogic::tickParticles);
		LOGGER.info("[Fabao] initialized");
	}
}
