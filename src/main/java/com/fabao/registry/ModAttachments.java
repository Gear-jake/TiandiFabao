package com.fabao.registry;

import com.fabao.FabaoMod;
import com.fabao.cultivation.CultivationData;
import com.fabao.equipment.FabaoSlots;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

/**
 * 玩家侧持久数据：灵力 + 法宝槽选中状态。
 * 只做服务端持久化与死亡保留，**不做 attachment 自动同步**——
 * Fabric 的同步包在玩家重进存档时会指向客户端不存在的实体 ID，
 * 导致 "Unknown target for attachment change" 断连。
 * 客户端需要的状态（轮盘选中、装备数量）改由本地缓存与 Accessories 数据提供。
 */
public final class ModAttachments {
	public static final AttachmentType<CultivationData> CULTIVATION = AttachmentRegistry.create(
			FabaoMod.id("cultivation"), builder -> builder
					.persistent(CultivationData.CODEC)
					.copyOnDeath()
					.initializer(() -> CultivationData.DEFAULT));

	public static final AttachmentType<FabaoSlots> SLOTS = AttachmentRegistry.create(
			FabaoMod.id("fabao_slots"), builder -> builder
					.persistent(FabaoSlots.CODEC)
					.copyOnDeath()
					.initializer(FabaoSlots::empty));

	private ModAttachments() {
	}

	/** 触发静态字段完成注册（AttachmentRegistry.create 即注册）。 */
	public static void init() {
	}
}
