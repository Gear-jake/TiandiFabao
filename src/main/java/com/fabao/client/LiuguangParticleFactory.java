package com.fabao.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public final class LiuguangParticleFactory implements ParticleFactory<SimpleParticleType> {
	private final SpriteProvider spriteProvider;

	public LiuguangParticleFactory(SpriteProvider spriteProvider) {
		this.spriteProvider = spriteProvider;
	}

	@Override
	public Particle createParticle(SimpleParticleType parameters, ClientWorld world,
								   double x, double y, double z,
								   double velocityX, double velocityY, double velocityZ, Random random) {
		return new LiuguangParticle(world, x, y, z, velocityX, velocityY, velocityZ,
				spriteProvider.getSprite(random));
	}
}
