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
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class FootstepParticle extends Particle {
	private static final float radians = (float)Math.PI / 180F;

	private final TextureAtlasSprite sprite;
	private final float rot;

	protected FootstepParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
		super(clientLevel, d, e, f);
		this.xd = 0.0;
		this.yd = 0.0;
		this.zd = 0.0;
		this.rot = radians * (float)g;
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
		float alpha = Mth.lerp((Math.min(this.age + partialTicks, (float)this.lifetime)) / (float)this.lifetime, 1F, 0F);
		Vec3 vec3 = renderInfo.getPosition();
		int l = this.getLightColor(partialTicks);
		float u0 = this.sprite.getU0();
		float u1 = this.sprite.getU1();
		float v0 = this.sprite.getV0();
		float v1 = this.sprite.getV1();
		Matrix4f matrix4f = new Matrix4f().translation((float)(this.x - vec3.x), (float)(this.y - vec3.y), (float)(this.z - vec3.z));
		matrix4f.rotate(this.rot, 0.0f, 1.0f, 0.0f);
		buffer.vertex(matrix4f, -0.125f, 0.0f, 0.125f).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, alpha).uv2(l).endVertex();
		buffer.vertex(matrix4f, 0.125f, 0.0f, 0.125f).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, alpha).uv2(l).endVertex();
		buffer.vertex(matrix4f, 0.125f, 0.0f, -0.125f).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, alpha).uv2(l).endVertex();
		buffer.vertex(matrix4f, -0.125f, 0.0f, -0.125f).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, alpha).uv2(l).endVertex();
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

