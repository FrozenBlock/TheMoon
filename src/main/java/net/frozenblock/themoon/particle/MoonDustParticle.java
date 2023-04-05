package net.frozenblock.themoon.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MoonDustParticle extends SmokeParticle {
	protected MoonDustParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, 2.5f, spriteProvider);
		this.rCol = 1.0f;
		this.gCol = 1.0f;
		this.bCol = 1.0f;
	}

	@Environment(value=EnvType.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteProvider) {
			this.sprites = spriteProvider;
		}

		@Override
		public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel world, double d, double e, double f, double g, double h, double i) {
			return new MoonDustParticle(world, d, e, f, g, h, i, this.sprites);
		}
	}
}
