package com.fabao.combat;

import com.fabao.equipment.FabaoActivator;
import com.fabao.util.ParticleFx;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 召唤护主（器灵）：满蓄 + 潜行释放触发。用原版 ItemDisplayEntity 展示法宝本体，
 * 服务端脚本驱动（环绕主人 + 每 15 tick 劈最近敌对），8 秒归鞘。
 * 零自定义实体类型、零客户端渲染器 —— 规避 1.21.9+ 渲染重写的全部风险（DESIGN §6）。
 */
public final class QilingManager {
	private static final int DURATION_TICKS = 160;
	private static final int ATTACK_INTERVAL = 15;
	private static final Map<UUID, Qiling> ACTIVE = new HashMap<>();

	private record Qiling(UUID displayId, int startTick, float damage) {
	}

	private QilingManager() {
	}

	public static void summon(ServerPlayerEntity player, ItemStack fabaoStack, float damage) {
		despawn(player);
		ServerWorld world = player.getEntityWorld();
		DisplayEntity.ItemDisplayEntity display = new DisplayEntity.ItemDisplayEntity(EntityType.ITEM_DISPLAY, world);
		display.getStackReference(0).set(fabaoStack.copy());
		display.setPosition(player.getX(), player.getY() + 1.6, player.getZ());
		world.spawnEntity(display);
		ACTIVE.put(player.getUuid(), new Qiling(display.getUuid(), world.getServer().getTicks(), damage));
		player.sendMessage(Text.translatable("fabao.qiling.summon"), false);
	}

	public static void despawn(ServerPlayerEntity player) {
		Qiling qiling = ACTIVE.remove(player.getUuid());
		if (qiling == null) {
			return;
		}
		Entity entity = player.getEntityWorld().getEntity(qiling.displayId());
		if (entity != null) {
			entity.discard();
		}
		player.sendMessage(Text.translatable("fabao.qiling.despawn"), true);
	}

	public static void onEndTick(MinecraftServer server) {
		if (ACTIVE.isEmpty()) {
			return;
		}
		Iterator<Map.Entry<UUID, Qiling>> it = ACTIVE.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<UUID, Qiling> entry = it.next();
			ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
			if (player == null) {
				it.remove();
				continue;
			}
			ServerWorld world = player.getEntityWorld();
			Entity entity = world.getEntity(entry.getValue().displayId());
			if (!(entity instanceof DisplayEntity.ItemDisplayEntity display)) {
				it.remove();
				continue;
			}
			int age = server.getTicks() - entry.getValue().startTick();
			if (age > DURATION_TICKS) {
				display.discard();
				player.sendMessage(Text.translatable("fabao.qiling.despawn"), true);
				it.remove();
				continue;
			}
			double angle = age * 0.3;
			double x = player.getX() + Math.cos(angle) * 1.5;
			double y = player.getY() + 1.6;
			double z = player.getZ() + Math.sin(angle) * 1.5;
			display.requestTeleport(x, y, z);
			ParticleFx.trail(world, new Vec3d(x, y, z));
			if (age % ATTACK_INTERVAL == 0) {
				LivingEntity target = FabaoActivator.nearestHostile(player, world, 12.0);
				if (target != null) {
					FabaoActivator.hurt(player, world, target, entry.getValue().damage());
					FabaoActivator.spawnCosmeticBolt(world, target);
					ParticleFx.burst(world, target, ParticleFx.GOLD);
				}
			}
		}
	}
}
