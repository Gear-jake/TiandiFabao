package com.fabao.util;

import com.fabao.registry.ModParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

/**
 * 战斗特效封装。
 * 性能策略：形状粒子一律用「批量 spawnParticles」——每个调用广播 1 个包，
 * 包内粒子数可以很大（几十个）而不增加网络开销。所以视觉效果靠「每包更多粒子 + 多层」，
 * 而不是「更多包」。
 */
public final class ParticleFx {
	public static final int GOLD = 0xFFD8A843;
	public static final int RED = 0xFFC43C34;
	public static final int THUNDER = 0xFF9B7CFF;
	public static final int WHITE = 0xFFF0F0F0;
	public static final int GREEN = 0xFF6EC8B8;
	public static final int ORANGE = 0xFFFF9A32;
	public static final int PURPLE = 0xFF9660C4;
	public static final int CYAN = 0xFF7FE7FF;

	private ParticleFx() {
	}

	private static DustParticleEffect dust(int color, float scale) {
		return new DustParticleEffect(color, scale);
	}

	/** 一次性随机散布（多彩 + 闪光层次）。 */
	public static void dust(ServerWorld world, Vec3d pos, int color, int count, double spread) {
		world.spawnParticles(dust(color, 1.3f), pos.x, pos.y, pos.z, count, spread, spread, spread, 0.02);
		world.spawnParticles(dust(WHITE, 0.7f), pos.x, pos.y, pos.z, Math.max(3, count / 2), spread * 1.2, spread * 1.2, spread * 1.2, 0.01);
	}

	/** 环形范围指示：4 层同心环 + 中心爆发（5 个包，约 70 粒子）。 */
	public static void ring(ServerWorld world, Vec3d center, double radius, int count, int color) {
		world.spawnParticles(dust(color, 1.5f), center.x, center.y + 0.15, center.z,
				Math.max(10, count), radius, 0.02, radius, 0.0);
		world.spawnParticles(dust(color, 1.3f), center.x, center.y + 0.4, center.z,
				Math.max(8, count / 2), radius * 0.7, 0.03, radius * 0.7, 0.0);
	}

	/** 前方扇形：5 段推进 + 尖端爆点（6 个包，约 80 粒子）。 */
	public static void cone(ServerWorld world, ServerPlayerEntity player, double radius, int count, int color) {
		Vec3d look = player.getRotationVec(1.0f);
		Vec3d origin = player.getEntityPos().add(0, 0.9, 0);
		int perRing = Math.max(5, count / 2);
		for (int ring = 1; ring <= 3; ring++) {
			double t = ring / 3.0;
			Vec3d at = origin.add(look.multiply(radius * t));
			float scale = 1.6f - 0.5f * (float) t;
			world.spawnParticles(dust(color, scale), at.x, at.y, at.z,
					perRing, radius * 0.2 * (0.6 + t), 0.3, radius * 0.2 * (0.6 + t), 0.02);
			world.spawnParticles(dust(WHITE, 0.8f), at.x, at.y + 0.25, at.z,
					Math.max(4, perRing / 2), radius * 0.12, 0.2, radius * 0.12, 0.01);
		}
		Vec3d tip = origin.add(look.multiply(radius));
		world.spawnParticles(ParticleTypes.CRIT, tip.x, tip.y, tip.z, 18, 0.35, 0.35, 0.35, 0.12);
	}

	/** 直线轨迹：沿线双色交织（约 10 个包，但每包多粒子）。 */
	public static void line(ServerWorld world, Vec3d from, Vec3d to, int count, int color) {
		int points = Math.max(6, count);
		for (int i = 0; i < points; i++) {
			Vec3d pos = from.lerp(to, i / (double) (points - 1));
			world.spawnParticles(dust(color, 1.4f), pos.x, pos.y, pos.z, 3, 0.05, 0.05, 0.05, 0.01);
			if (i % 2 == 0) {
				world.spawnParticles(dust(WHITE, 0.8f), pos.x, pos.y, pos.z, 1, 0.08, 0.08, 0.08, 0.01);
			}
		}
	}

	/** 命中爆点：厚实的受击反馈（4 个包，约 60 粒子）。 */
	public static void burst(ServerWorld world, LivingEntity target, int color) {
		Vec3d pos = target.getEntityPos().add(0, target.getHeight() * 0.5, 0);
		world.spawnParticles(dust(color, 1.9f), pos.x, pos.y, pos.z, 14, 0.28, 0.3, 0.28, 0.05);
		world.spawnParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 8, 0.22, 0.25, 0.22, 0.12);
	}

	/** 天降光柱：5 段 + 底部冲击环（6 个包，约 70 粒子）。 */
	public static void column(ServerWorld world, Vec3d base, double height, int color) {
		for (int i = 0; i < 3; i++) {
			double t = i / 3.0;
			double y = base.y + height * t;
			world.spawnParticles(dust(color, 1.7f), base.x, y, base.z, 6, 0.3, 0.12, 0.3, 0.02);
		}
		world.spawnParticles(dust(color, 1.3f), base.x, base.y + 0.1, base.z, 14, 0.85, 0.02, 0.85, 0.0);
	}

	/** 拖尾（器灵飞行，1 个包）。 */
	public static void trail(ServerWorld world, Vec3d pos) {
		world.spawnParticles(ModParticles.LIUGUANG, pos.x, pos.y, pos.z, 2, 0.05, 0.05, 0.05, 0.01);
	}

	/** 蓄力螺旋：绕玩家上升的光点（4 个包），用于蓄力阶段。 */
	public static void chargeSpiral(ServerWorld world, ServerPlayerEntity player, int color, float progress) {
		Vec3d base = player.getEntityPos().add(0, 0.2, 0);
		int steps = 12;
		for (int band = 0; band < 3; band++) {
			double y = base.y + band * 0.45;
			double r = 0.7 - band * 0.12;
			world.spawnParticles(dust(color, 1.3f), base.x, y, base.z, steps, r, 0.06, r, 0.0);
		}
		world.spawnParticles(ParticleTypes.END_ROD, base.x, base.y + 1.2, base.z,
				6, 0.35, 0.25, 0.35, 0.02 + progress * 0.05);
	}

	/** 地面冲击环（2 个包），用于大招落地。 */
	public static void shockwave(ServerWorld world, Vec3d center, double radius, int color) {
		world.spawnParticles(dust(color, 1.9f), center.x, center.y + 0.08, center.z,
				Math.max(24, (int) (radius * 18)), radius, 0.01, radius, 0.0);
		world.spawnParticles(dust(WHITE, 1.0f), center.x, center.y + 0.2, center.z,
				Math.max(12, (int) (radius * 10)), radius * 0.7, 0.02, radius * 0.7, 0.0);
	}
}
