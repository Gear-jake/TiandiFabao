package com.fabao.equipment;

import com.fabao.FabaoMod;
import com.fabao.item.FabaoItem;
import com.fabao.item.FabaoStats;
import com.fabao.item.FabaoType;
import com.fabao.registry.ModItems;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.core.Accessory;
import io.wispforest.accessories.api.core.AccessoryRegistry;
import io.wispforest.accessories.api.slot.SlotBasedPredicate;
import io.wispforest.accessories.api.slot.SlotPredicateRegistry;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 法宝与 Accessories 饰品栏的桥：法宝作为饰品注册，被动属性交给框架的动态修饰符机制。
 * 栏位谓词 fabao:fabao_only 只放行 FabaoItem（槽定义在
 * data/fabao/accessories/slot/fabao.json，validators 引用该谓词）。
 */
public final class FabaoAccessory implements Accessory {
	public static final FabaoAccessory INSTANCE = new FabaoAccessory();

	private FabaoAccessory() {
	}

	public static void init() {
		// 只收法器：兵器型法宝是手持武器，不进饰品槽
		SlotPredicateRegistry.register(FabaoMod.id("fabao_only"),
				SlotBasedPredicate.ofItem(item -> item instanceof FabaoItem f && !f.fabaoType().isWeapon()));
		for (Item item : ModItems.FABAO_ITEMS) {
			if (item instanceof FabaoItem fabao && !fabao.fabaoType().isWeapon()) {
				AccessoryRegistry.register(item, INSTANCE);
			}
		}
	}

	/** 右键装备完全交给 Accessories 框架（其他饰品模组的标准做法）。 */
	@Override
	public boolean canEquipFromUse(ItemStack stack, SlotReference reference) {
		return true;
	}

	@Override
	public boolean canEquip(ItemStack stack, SlotReference reference) {
		if (!(stack.getItem() instanceof FabaoItem fabao)) {
			return false;
		}
		// 兵器型（手持武器）不允许装进饰品槽
		if (fabao.fabaoType().isWeapon()) {
			return false;
		}
		if (reference == null || !(reference.entity() instanceof ServerPlayerEntity player)) {
			// 客户端预检/非玩家实体：放行，服务端装备动作会二次校验
			return true;
		}
		// 认主法宝只归主：他人无法装配
		return !FabaoStats.isBound(stack) || FabaoStats.isBoundTo(stack, player);
	}

	/**
	 * 常驻被动：被动基值 × (1+0.1×品阶) × 词条 × 认主1.2。
	 * 品阶/词条变化后需重新装备一次才能刷新（祭炼在副手进行，天然满足）。
	 */
	@Override
	public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
		if (!(stack.getItem() instanceof FabaoItem fabao)) {
			return;
		}
		double value = fabao.fabaoType().passive().value() * multiplier(stack, reference);
		builder.addStackable(attributeHolder(fabao.fabaoType().passive()),
				FabaoMod.id("passive_" + fabao.fabaoType().passive().attributePath()),
				value,
				fabao.fabaoType().passive().multiplicative()
						? EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
						: EntityAttributeModifier.Operation.ADD_VALUE);
	}

	private static float multiplier(ItemStack stack, SlotReference reference) {
		if (reference != null && reference.entity() instanceof PlayerEntity player) {
			return FabaoStats.multiplier(stack, player);
		}
		return 1.0f + 0.1f * FabaoStats.tier(stack);
	}

	private static RegistryEntry<EntityAttribute> attributeHolder(FabaoType.Passive passive) {
		return switch (passive) {
			case ATTACK_SPEED -> EntityAttributes.ATTACK_SPEED;
			case MOVE_SPEED -> EntityAttributes.MOVEMENT_SPEED;
			case ATTACK_DAMAGE -> EntityAttributes.ATTACK_DAMAGE;
			case MAX_HEALTH -> EntityAttributes.MAX_HEALTH;
			case ARMOR -> EntityAttributes.ARMOR;
			case KNOCKBACK_RESIST -> EntityAttributes.KNOCKBACK_RESISTANCE;
		};
	}
}
