package com.fabao.combat;

import com.fabao.cultivation.CultivationManager;
import com.fabao.equipment.FabaoActivator;
import com.fabao.item.FabaoType;
import com.fabao.registry.ModSounds;
import com.fabao.util.ParticleFx;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 连招组合：8 秒（160 tick）窗口内连续释放 3 件不同法宝 → 「三才合璧」：
 * 15 格 AOE（伤害随境界成长）+ 编钟齐鸣 + 落雷特效。触发后清空连击记录。
 */
public final class ComboTracker {
	private static final int WINDOW_TICKS = 160;
	private static final Map<UUID, Deque<Entry>> HISTORY = new HashMap<>();

	private record Entry(FabaoType type, int tick) {
	}

	private ComboTracker() {
	}

	public static void record(ServerPlayerEntity player, FabaoType type) {
		int now = player.getEntityWorld().getServer().getTicks();
		Deque<Entry> deque = HISTORY.computeIfAbsent(player.getUuid(), k -> new ArrayDeque<>());
		deque.addLast(new Entry(type, now));
		while (!deque.isEmpty() && now - deque.peekFirst().tick() > WINDOW_TICKS) {
			deque.pollFirst();
		}
		if (deque.size() < 3) {
			return;
		}
		List<Entry> all = new ArrayList<>(deque);
		List<Entry> lastThree = all.subList(all.size() - 3, all.size());
		Set<FabaoType> distinct = new HashSet<>();
		for (Entry entry : lastThree) {
			distinct.add(entry.type());
		}
		if (distinct.size() == 3) {
			triggerCombo(player);
			deque.clear();
		}
	}

	private static void triggerCombo(ServerPlayerEntity player) {
		ServerWorld world = player.getEntityWorld();
		float damage = 30.0f + 10.0f * CultivationManager.equippedCount(player);
		for (LivingEntity enemy : FabaoActivator.hostilesAround(player, world, 15.0)) {
			FabaoActivator.hurt(player, world, enemy, damage);
			FabaoActivator.spawnCosmeticBolt(world, enemy);
		}
		ParticleFx.burst(world, player, ParticleFx.GOLD);
		world.playSound(null, player.getX(), player.getY(), player.getZ(),
				ModSounds.ZHONGQING_RELEASE, SoundCategory.PLAYERS, 1.6f, 0.7f);
		world.playSound(null, player.getX(), player.getY(), player.getZ(),
				SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0f, 1.2f);
		player.sendMessage(Text.translatable("fabao.combo.trigger"), false);
	}
}
