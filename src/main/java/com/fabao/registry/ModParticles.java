package com.fabao.registry;

import com.fabao.FabaoMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/** 流光粒子（金色拖尾）。 */
public final class ModParticles {
	public static final SimpleParticleType LIUGUANG = Registry.register(
			Registries.PARTICLE_TYPE, FabaoMod.id("liuguang"), FabricParticleTypes.simple(true));

	private ModParticles() {
	}

	public static void init() {
	}
}
