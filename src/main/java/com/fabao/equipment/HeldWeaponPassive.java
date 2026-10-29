package com.fabao.equipment;

import com.fabao.FabaoMod;
import com.fabao.item.FabaoItem;
import com.fabao.item.FabaoStats;
import com.fabao.item.FabaoType;
import com.fabao.registry.ModAttachments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 兵器型法宝的手持被动：拿在手上就生效加成（每 20 tick 对账，值不变不动，避免生命上限反复夹血）。
 * 法器型的被动仍由 Accessories 的 getDynamicModifiers 处理。
 */
public final class HeldWeaponPassive {
	private static final Identifier MODIFIER_ID = FabaoMod.id("held_weapon_passive");
	private record Applied(RegistryEntry<EntityAttribute> attribute, double value) {
	}

	private static final Map<UUID, Applied> APPLIED = new HashMap<>();

	private HeldWeaponPassive() {
	}

	public static void onEndTick(MinecraftServer server) {
		if (server.getTicks() % 20 != 0) {
			return;
		}
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			ItemStack main = player.getMainHandStack();
			if (main.getItem() instanceof FabaoItem fabao && fabao.fabaoType().isWeapon()) {
				FabaoType.Passive passive = fabao.fabaoType().passive();
				RegistryEntry<EntityAttribute> attribute = attributeOf(passive);
				double value = passive.value() * FabaoStats.multiplier(main, player);
				Applied applied = APPLIED.get(player.getUuid());
				if (applied == null || applied.attribute() != attribute || applied.value() != value) {
					if (applied != null) {
						removeModifier(player, applied.attribute());
					}
					EntityAttributeInstance instance = player.getAttributeInstance(attribute);
					if (instance != null) {
						instance.addTemporaryModifier(new EntityAttributeModifier(MODIFIER_ID, value,
								passive.multiplicative()
										? EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
										: EntityAttributeModifier.Operation.ADD_VALUE));
					}
					APPLIED.put(player.getUuid(), new Applied(attribute, value));
				}
			} else if (APPLIED.remove(player.getUuid()) != null) {
				clearAll(player);
			}
		}
	}

	private static void clearAll(ServerPlayerEntity player) {
		for (RegistryEntry<EntityAttribute> attr : new RegistryEntry[]{
				EntityAttributes.ATTACK_DAMAGE, EntityAttributes.ATTACK_SPEED,
				EntityAttributes.MAX_HEALTH, EntityAttributes.ARMOR,
				EntityAttributes.MOVEMENT_SPEED, EntityAttributes.KNOCKBACK_RESISTANCE}) {
			EntityAttributeInstance instance = player.getAttributeInstance(attr);
			if (instance != null) {
				instance.removeModifier(MODIFIER_ID);
			}
		}
	}

	private static void removeModifier(ServerPlayerEntity player, RegistryEntry<EntityAttribute> attribute) {
		EntityAttributeInstance instance = player.getAttributeInstance(attribute);
		if (instance != null) {
			instance.removeModifier(MODIFIER_ID);
		}
	}

	private static RegistryEntry<EntityAttribute> attributeOf(FabaoType.Passive passive) {
		return switch (passive) {
			case ATTACK_SPEED -> EntityAttributes.ATTACK_SPEED;
			case MOVE_SPEED -> EntityAttributes.MOVEMENT_SPEED;
			case ATTACK_DAMAGE -> EntityAttributes.ATTACK_DAMAGE;
			case MAX_HEALTH -> EntityAttributes.MAX_HEALTH;
			case ARMOR -> EntityAttributes.ARMOR;
			case KNOCKBACK_RESIST -> EntityAttributes.KNOCKBACK_RESISTANCE;
		};
	}

	public static void clearPlayer(java.util.UUID id) {
		APPLIED.remove(id);
	}
}
