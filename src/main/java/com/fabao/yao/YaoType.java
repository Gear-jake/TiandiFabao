package com.fabao.yao;

import com.fabao.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.LivingEntity;

/**
 * 妖定义（原版实体模板 + 妖名 + 属性重调，v1 不写自定义实体渲染器）。
 * tier：1 小妖（原版装备可打）/ 2 大妖（需小妖丹法宝）/ 3 妖王（需大妖丹法宝）。
 * 特殊机制见 {@link YaoManager}：按实体 age 周期触发，还原原著桥段。
 */
public enum YaoType {
	FOX_YAO("fox_yao", ModEntities.FOX_DEMON, 1, 16.0f, 2.0f, YaoManager::mechCharm),
	ZHIZHU_JING("zhizhu_jing", ModEntities.SPIDER_DEMON, 1, 12.0f, 3.0f, YaoManager::mechWeave),
	HEIXIONG_JING("heixiong_jing", ModEntities.BEAR_DEMON, 1, 20.0f, 4.0f, YaoManager::mechCharge),

	BAIGU_JING("baigu_jing", EntityType.SKELETON, 2, 60.0f, 7.0f, YaoManager::mechDrain),
	JINJIAO_DAWANG("jinjiao_dawang", EntityType.EVOKER, 2, 50.0f, 6.0f, YaoManager::mechGourd),
	YINJIAO_DAWANG("yinjiao_dawang", EntityType.VINDICATOR, 2, 45.0f, 8.0f, YaoManager::mechRope),

	LIUER_MIHOU("liuer_mihou", EntityType.ILLUSIONER, 3, 120.0f, 11.0f, YaoManager::mechIllusion),
	DAPENG("dapeng", EntityType.PHANTOM, 3, 150.0f, 12.0f, YaoManager::mechDive);

	private final String id;
	private final EntityType<?> template;
	private final int tier;
	private final float maxHealth;
	private final float attackDamage;
	private final Mechanic mechanic;

	YaoType(String id, EntityType<?> template, int tier, float maxHealth, float attackDamage, Mechanic mechanic) {
		this.id = id;
		this.template = template;
		this.tier = tier;
		this.maxHealth = maxHealth;
		this.attackDamage = attackDamage;
		this.mechanic = mechanic;
	}

	public String id() {
		return id;
	}

	public EntityType<?> template() {
		return template;
	}

	public int tier() {
		return tier;
	}

	public float maxHealth() {
		return maxHealth;
	}

	public float attackDamage() {
		return attackDamage;
	}

	public void tickMechanic(LivingEntity self, ServerPlayerEntity player) {
		mechanic.tick(self, player);
	}

	public static YaoType byId(String id) {
		for (YaoType type : values()) {
			if (type.id.equals(id)) {
				return type;
			}
		}
		return null;
	}

	public static java.util.List<YaoType> byTier(int tier) {
		java.util.List<YaoType> list = new java.util.ArrayList<>();
		for (YaoType type : values()) {
			if (type.tier == tier) {
				list.add(type);
			}
		}
		return list;
	}

	@FunctionalInterface
	public interface Mechanic {
		void tick(LivingEntity self, ServerPlayerEntity player);
	}
}
