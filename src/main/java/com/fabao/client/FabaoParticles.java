package com.fabao.client;

import com.fabao.registry.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public final class FabaoParticles {
	private FabaoParticles() {
	}

	public static void init() {
		ParticleFactoryRegistry.getInstance()
				.register(ModParticles.LIUGUANG, provider -> new LiuguangParticleFactory(provider));
	}
}
