package net.frozenblock.themoon.entity.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.themoon.TheMoonModClient;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.entity.render.model.AsteroidModel;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class AsteroidRenderer extends MobRenderer<Asteroid, AsteroidModel<Asteroid>> {

	public AsteroidRenderer(Context context) {
		super(context, new AsteroidModel<>(context.bakeLayer(TheMoonModClient.ASTEROID)), 0.6F);
	}

	@Override
	public Vec3 getRenderOffset(@NotNull Asteroid entity, float partialTicks) {
		return new Vec3(0, 0.2375, 0);
	}

	@Override
	public void render(@NotNull Asteroid entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		float scale = entity.getScale() * 1.75F;
		poseStack.scale(scale, scale, scale);
		super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
		poseStack.popPose();
	}

	@Override
	protected void setupRotations(@NotNull Asteroid entityLiving, @NotNull PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTick) {

	}

	@Override
	@NotNull
	public ResourceLocation getTextureLocation(@NotNull Asteroid entity) {
		return TheMoonSharedConstants.id("textures/entity/asteroid/asteroid.png");
	}

}
