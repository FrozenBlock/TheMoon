package net.frozenblock.themoon.world.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class ExosphereSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {
	private static final float SUN_SIZE = 30F;
	private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
	private static final float MOON_SIZE = 30F;
	private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");
	private static final float EARTH_SIZE = 110F;
	private static final ResourceLocation EARTH_LOCATION = TheMoonSharedConstants.id("textures/environment/earth.png");
	private static final float NOON_TIME = 6000F;
	private static final float MIDNIGHT_TIME = 18000F;
	private static final float NOON_TIME_FIXED = timeOfDay(6000);
	private static final float MIDNIGHT_TIME_FIXED = timeOfDay(18000);
	private static final double WORLD_WIDTH = 30000000;
	private static final double WORLD_LENGTH = 60000000;

	@Nullable
	private VertexBuffer skyBuffer;
	@Nullable
	private VertexBuffer darkBuffer;
	@Nullable
	private VertexBuffer starBuffer;

	private boolean setup;

	private void setup() {
		if (!this.setup) {
			this.setup = true;
			this.createStars();
			this.createLightSky();
			this.createDarkSky();
		}
	}

	@Override
	public void render(WorldRenderContext context) {
		float tickDelta = context.tickDelta();
		ClientLevel level = context.world();
		PoseStack poseStack = context.matrixStack();
		Matrix4f matrix = context.projectionMatrix();
		Camera camera = context.camera();
		GameRenderer gameRenderer = context.gameRenderer();
		if (level == null || matrix == null) {
			return;
		}
		FogType fogType = camera.getFluidInCamera();
		if (fogType == FogType.POWDER_SNOW || fogType == FogType.LAVA || doesMobEffectBlockSky(camera)) {
			return;
		}
		this.setup();
		float q;
		float p;
		float o;
		float k;
		float i;
		Vec3 vec3 = level.getSkyColor(gameRenderer.getMainCamera().getPosition(), tickDelta);
		float f = (float)vec3.x;
		float g = (float)vec3.y;
		float h = (float)vec3.z;
		FogRenderer.levelFogColor();
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(f, g, h, 1.0f);
		ShaderInstance shaderInstance = RenderSystem.getShader();
		this.skyBuffer.bind();
		this.skyBuffer.drawWithShader(poseStack.last().pose(), matrix, shaderInstance);
		VertexBuffer.unbind();
		RenderSystem.enableBlend();
		float[] fs = level.effects().getSunriseColor(level.dimensionType().timeOfDay(level.dayTime() + 12000), tickDelta);
		if (fs != null) {
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
			i = Mth.sin(this.getSunAngle(level)) < 0.0f ? 180.0f : 0.0f;
			poseStack.mulPose(Axis.ZP.rotationDegrees(i));
			poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
			float j = fs[0];
			k = fs[1];
			float l = fs[2];
			Matrix4f matrix4f2 = poseStack.last().pose();
			bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
			bufferBuilder.vertex(matrix4f2, 0.0f, 100.0f, 0.0f).color(j, k, l, fs[3]).endVertex();
			for (int n = 0; n <= 16; ++n) {
				o = (float)n * ((float)Math.PI * 2) / 16.0f;
				p = Mth.sin(o);
				q = Mth.cos(o);
				bufferBuilder.vertex(matrix4f2, p * 120.0f, q * 120.0f, -q * 40.0f * fs[3]).color(fs[0], fs[1], fs[2], 0.0f).endVertex();
			}
			BufferUploader.drawWithShader(bufferBuilder.end());
			poseStack.popPose();
		}
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		poseStack.pushPose();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		//SUN & MOON & EARTH

		Vec3 playerPos = camera.getPosition();

		poseStack.mulPose(Axis.YP.rotationDegrees(-90F));

		poseStack.pushPose();
		float rotation = level.dimensionType().timeOfDay(level.dayTime());
		poseStack.mulPose(Axis.XP.rotationDegrees(rotation * 360F));
		Matrix4f matrix4f3 = poseStack.last().pose();

		k = SUN_SIZE;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, SUN_LOCATION);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f3, -k, 100F, -k).uv(0.0f, 0.0f).endVertex();
		bufferBuilder.vertex(matrix4f3, k, 100F, -k).uv(1.0f, 0.0f).endVertex();
		bufferBuilder.vertex(matrix4f3, k, 100F, k).uv(1.0f, 1.0f).endVertex();
		bufferBuilder.vertex(matrix4f3, -k, 100F, k).uv(0.0f, 1.0f).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());

		k = MOON_SIZE;
		RenderSystem.setShaderTexture(0, MOON_LOCATION);
		int r = level.getMoonPhase();
		int s = r % 4;
		int m = r / 4 % 2;
		float t = (float)s / 4.0F;
		o = (float)m / 2.0F;
		p = (float)(s + 1) / 4.0F;
		q = (float)(m + 1) / 2.0F;
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f3, -k, -100F, k).uv(p, q).endVertex();
		bufferBuilder.vertex(matrix4f3, k, -100F, k).uv(t, q).endVertex();
		bufferBuilder.vertex(matrix4f3, k, -100F, -k).uv(t, o).endVertex();
		bufferBuilder.vertex(matrix4f3, -k, -100F, -k).uv(p, o).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		poseStack.popPose();

		matrix4f3 = poseStack.last().pose();
		k = EARTH_SIZE;
		RenderSystem.setShaderTexture(0, EARTH_LOCATION);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f3, -k, 90F, k).uv(0.0f, 0.0f).endVertex();
		bufferBuilder.vertex(matrix4f3, k, 90F, k).uv(1.0f, 0.0f).endVertex();
		bufferBuilder.vertex(matrix4f3, k, 90F, -k).uv(1.0f, 1.0f).endVertex();
		bufferBuilder.vertex(matrix4f3, -k, 90F, -k).uv(0.0f, 1.0f).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());

		//STARS

		RenderSystem.setShaderColor(255F, 255F, 255F, 255F);
		FogRenderer.setupNoFog();
		this.starBuffer.bind();
		this.starBuffer.drawWithShader(poseStack.last().pose(), matrix, GameRenderer.getPositionShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		poseStack.popPose();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.depthMask(true);

		GravityBeltRenderer.renderGravityBelts(level, camera, poseStack);
	}

	public float getSunAngle(ClientLevel level) {
		float f = level.dimensionType().timeOfDay(level.dayTime() + 12000);
		return f * ((float)Math.PI * 2);
	}

	private static float getSkyOffset(double d) {
		float progress = (float) ((d + WORLD_WIDTH) / WORLD_LENGTH);
		return timeOfDay((long) ((long) Mth.lerp(progress, -MIDNIGHT_TIME, MIDNIGHT_TIME) + NOON_TIME));
	}

	private void createDarkSky() {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		if (this.darkBuffer != null) {
			this.darkBuffer.close();
		}
		this.darkBuffer = new VertexBuffer();
		BufferBuilder.RenderedBuffer renderedBuffer = buildSkyDisc(bufferBuilder, -16.0f);
		this.darkBuffer.bind();
		this.darkBuffer.upload(renderedBuffer);
		VertexBuffer.unbind();
	}

	private void createLightSky() {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		if (this.skyBuffer != null) {
			this.skyBuffer.close();
		}
		this.skyBuffer = new VertexBuffer();
		BufferBuilder.RenderedBuffer renderedBuffer = buildSkyDisc(bufferBuilder, 16.0f);
		this.skyBuffer.bind();
		this.skyBuffer.upload(renderedBuffer);
		VertexBuffer.unbind();
	}

	private static BufferBuilder.RenderedBuffer buildSkyDisc(BufferBuilder buffer, float y) {
		float f = Math.signum(y) * 512.0f;
		RenderSystem.setShader(GameRenderer::getPositionShader);
		buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
		buffer.vertex(0.0, y, 0.0).endVertex();
		for (int i = -180; i <= 180; i += 45) {
			buffer.vertex(f * Mth.cos((float)i * ((float)Math.PI / 180)), y, 512.0f * Mth.sin((float)i * ((float)Math.PI / 180))).endVertex();
		}
		return buffer.end();
	}

	private void createStars() {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionShader);
		if (this.starBuffer != null) {
			this.starBuffer.close();
		}
		this.starBuffer = new VertexBuffer();
		BufferBuilder.RenderedBuffer renderedBuffer = this.drawStars(bufferBuilder);
		this.starBuffer.bind();
		this.starBuffer.upload(renderedBuffer);
		VertexBuffer.unbind();
	}

	private BufferBuilder.RenderedBuffer drawStars(BufferBuilder buffer) {
		RandomSource randomSource = RandomSource.create(10842L);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		for (int i = 0; i < 1500; ++i) {
			double d = randomSource.nextFloat() * 2.0f - 1.0f;
			double e = randomSource.nextFloat() * 2.0f - 1.0f;
			double f = randomSource.nextFloat() * 2.0f - 1.0f;
			double g = 0.15f + randomSource.nextFloat() * 0.1f;
			double h = d * d + e * e + f * f;
			if (!(h < 1.0) || !(h > 0.01)) continue;
			h = 1.0 / Math.sqrt(h);
			double j = (d *= h) * 100.0;
			double k = (e *= h) * 100.0;
			double l = (f *= h) * 100.0;
			double m = Math.atan2(d, f);
			double n = Math.sin(m);
			double o = Math.cos(m);
			double p = Math.atan2(Math.sqrt(d * d + f * f), e);
			double q = Math.sin(p);
			double r = Math.cos(p);
			double s = randomSource.nextDouble() * Math.PI * 2.0;
			double t = Math.sin(s);
			double u = Math.cos(s);
			for (int v = 0; v < 4; ++v) {
				double x = (double)((v & 2) - 1) * g;
				double y = (double)((v + 1 & 2) - 1) * g;
				double aa = x * u - y * t;
				double ac = y * u + x * t;
				double ad = aa * q + 0.0 * r;
				double ae = 0.0 * q - aa * r;
				double af = ae * n - ac * o;
				double ah = ac * n + ae * o;
				buffer.vertex(j + af, k + ad, l + ah).endVertex();
			}
		}
		return buffer.end();
	}

	private static boolean doesMobEffectBlockSky(Camera camera) {
		Entity entity = camera.getEntity();
		if (entity instanceof LivingEntity livingEntity) {
			return livingEntity.hasEffect(MobEffects.BLINDNESS) || livingEntity.hasEffect(MobEffects.DARKNESS);
		}
		return false;
	}

	private static float timeOfDay(long time) {
		double d = Mth.frac((double)time / 24000.0 - 0.25);
		double e = 0.5 - Math.cos(d * Math.PI) / 2.0;
		return (float)(d * 2.0 + e) / 3.0f;
	}
}
