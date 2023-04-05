package net.frozenblock.themoon.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class TheMoonParticleTypes {
	public static final SimpleParticleType MOON_DUST = register("moon_dust");

	public static void registerParticles() {
		TheMoonSharedConstants.logMod("Registering Particles for", TheMoonSharedConstants.UNSTABLE_LOGGING);
	}

	private static SimpleParticleType register(String name, boolean alwaysShow) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, TheMoonSharedConstants.id(name), FabricParticleTypes.simple(alwaysShow));
	}

	private static SimpleParticleType register(String name) {
		return register(name, false);
	}
}
