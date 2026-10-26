package com.fabao.client;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;

/** 流光粒子本体：金色短寿命光点，随年龄淡出。 */
public class LiuguangParticle extends BillboardParticle {
	private final float baseAlpha;

	protected LiuguangParticle(ClientWorld world, double x, double y, double z,
							   double vx, double vy, double vz, Sprite sprite) {
		super(world, x, y, z, vx, vy, vz, sprite);
		this.setMaxAge(18 + world.random.nextInt(14));
		this.scale(0.5f + world.random.nextFloat() * 0.3f);
		this.red = 1.0f;
		this.green = 0.85f;
		this.blue = 0.35f;
		this.alpha = 0.9f;
		this.baseAlpha = 0.9f;
	}

	@Override
	protected RenderType getRenderType() {
		return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();
		float life = 1.0f - (float) this.age / (float) this.maxAge;
		this.alpha = this.baseAlpha * Math.max(0.0f, life);
	}
}
