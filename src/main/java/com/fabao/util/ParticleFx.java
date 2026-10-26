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
 * 性能约束：形状粒子用「批量 spawnParticles」而不是逐点调用——
 * 每个 spawnParticles 调用都会广播一个粒子包，逐点画 40+ 点会明显拖慢服务端与网络。
 * 现在一次释放约 8-12 个包（原来 40-60 个）。
 */
public final class ParticleFx {
	public static final int GOLD = 0xFFD8A843;
	public static final int RED = 0xFFC43C34;
	public static final int THUNDER = 0xFF9B7CFF;
	public static final int WHITE = 0xFFF0F0F0;
	public static final int GREEN = 0xFF6EC8B8;
	public static final int ORANGE = 0xFFFF9A32;
	public static final int PURPLE = 0xFF9660C4;

	private ParticleFx() {
	}

	private static DustParticleEffect dust(int color, float scale) {
		return new DustParticleEffect(color, scale);
	}

	/** 一次性随机散布。 */
	public static void dust(ServerWorld world, Vec3d pos, int color, int count, double spread) {
		world.spawnParticles(dust(color, 1.2f), pos.x, pos.y, pos.z, count, spread, spread, spread, 0.02);
	}

	/** 环形范围指示：两层批量撒点（2 个包）。 */
	public static void ring(ServerWorld world, Vec3d center, double radius, int count, int color) {
		int outer = Math.max(8, count / 2);
		int inner = Math.max(4, count / 4);
		world.spawnParticles(dust(color, 1.4f), center.x, center.y + 0.25, center.z,
				outer, radius, 0.05, radius, 0.0);
		world.spawnParticles(dust(color, 1.2f), center.x, center.y + 0.5, center.z,
				inner, radius * 0.75, 0.05, radius * 0.75, 0.0);
	}

	/** 前方扇形：沿视线 3 段批量撒点（3 个包），方向感由采样点位置体现。 */
	public static void cone(ServerWorld world, ServerPlayerEntity player, double radius, int count, int color) {
		Vec3d look = player.getRotationVec(1.0f);
		Vec3d origin = player.getEntityPos().add(0, 0.9, 0);
		int perRing = Math.max(4, count / 3);
		for (int ring = 1; ring <= 3; ring++) {
			Vec3d at = origin.add(look.multiply(radius * ring / 3.0));
			world.spawnParticles(dust(color, 1.5f), at.x, at.y, at.z,
					perRing, radius * 0.22, 0.25, radius * 0.22, 0.01);
		}
	}

	/** 直线轨迹：批量撒点（约 5 个包）。 */
	public static void line(ServerWorld world, Vec3d from, Vec3d to, int count, int color) {
		int points = Math.max(3, count / 2);
		for (int i = 0; i < points; i++) {
			Vec3d pos = from.lerp(to, i / (double) (points - 1));
			world.spawnParticles(dust(color, 1.3f), pos.x, pos.y, pos.z, 1, 0.03, 0.03, 0.03, 0.01);
		}
	}

	/** 命中爆点：目标身上的受击反馈（2 个包）。 */
	public static void burst(ServerWorld world, LivingEntity target, int color) {
		Vec3d pos = target.getEntityPos().add(0, target.getHeight() * 0.5, 0);
		world.spawnParticles(dust(color, 1.7f), pos.x, pos.y, pos.z, 12, 0.28, 0.28, 0.28, 0.05);
		world.spawnParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 6, 0.2, 0.2, 0.2, 0.1);
	}

	/** 天降光柱：沿高度 3 段批量撒点（3 个包）。 */
	public static void column(ServerWorld world, Vec3d base, double height, int color) {
		for (int i = 0; i < 3; i++) {
			double y = base.y + height * i / 3.0;
			world.spawnParticles(dust(color, 1.5f), base.x, y, base.z, 6, 0.3, 0.15, 0.3, 0.01);
		}
	}

	/** 拖尾（器灵飞行，1 个包）。 */
	public static void trail(ServerWorld world, Vec3d pos) {
		world.spawnParticles(ModParticles.LIUGUANG, pos.x, pos.y, pos.z, 1, 0.04, 0.04, 0.04, 0.01);
	}
}
