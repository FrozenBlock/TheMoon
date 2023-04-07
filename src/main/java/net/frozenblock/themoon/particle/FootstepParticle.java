package net.frozenblock.themoon.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class FootstepParticle extends Particle {
	private final TextureAtlasSprite sprite;
	private final float rot;

	protected FootstepParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
		super(clientLevel, d, e, f);
		this.xd = 0.0;
		this.yd = 0.0;
		this.zd = 0.0;
		this.rot = (float)g;
		this.lifetime = 200;
		this.gravity = 0.0f;
		this.hasPhysics = false;
		this.sprite = spriteSet.get(this.random);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level.getBlockState(BlockPos.containing(this.x, this.y - 0.002, this.z)).is(TheMoonBlockTags.MOON_DUST)) {
			this.remove();
		}
	}

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		float f = ((float)this.age + partialTicks) / (float)this.lifetime;
		f *= f;
		float g = 2.0f - f * 2.0f;
		g *= 0.2f;
		float h = 0.125f;
		Vec3 vec3 = renderInfo.getPosition();
		float i = (float)(this.x - vec3.x);
		float j = (float)(this.y - vec3.y);
		float k = (float)(this.z - vec3.z);
		int l = this.getLightColor(partialTicks);
		float m = this.sprite.getU0();
		float n = this.sprite.getU1();
		float o = this.sprite.getV0();
		float p = this.sprite.getV1();
		Matrix4f matrix4f = new Matrix4f().translation(i, j, k);
		matrix4f.rotate((float)Math.PI / 180 * this.rot, 0.0f, 1.0f, 0.0f);
		buffer.vertex(matrix4f, -0.125f, 0.0f, 0.125f).uv(m, p).color(this.rCol, this.gCol, this.bCol, g).uv2(l).endVertex();
		buffer.vertex(matrix4f, 0.125f, 0.0f, 0.125f).uv(n, p).color(this.rCol, this.gCol, this.bCol, g).uv2(l).endVertex();
		buffer.vertex(matrix4f, 0.125f, 0.0f, -0.125f).uv(n, o).color(this.rCol, this.gCol, this.bCol, g).uv2(l).endVertex();
		buffer.vertex(matrix4f, -0.125f, 0.0f, -0.125f).uv(m, o).color(this.rCol, this.gCol, this.bCol, g).uv2(l).endVertex();
	}

	@Environment(value=EnvType.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		@Override
		public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
			return new FootstepParticle(clientLevel, d, e, f, g, h, i, this.sprites);
		}
	}
}

