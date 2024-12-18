package net.frozenblock.themoon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.frozenblock.themoon.entity.render.model.AsteroidModel;
import net.frozenblock.themoon.entity.render.renderer.AsteroidRenderer;
import net.frozenblock.themoon.particle.FootstepParticle;
import net.frozenblock.themoon.particle.MoonDustParticle;
import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.render.ExosphereSkyRenderer;
import net.frozenblock.themoon.world.render.MoonSkyRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class TheMoonModClient implements ClientModInitializer {
	public static final ModelLayerLocation ASTEROID = new ModelLayerLocation(TheMoonSharedConstants.id("asteroid"), "main");

    @Override
    public void onInitializeClient() {
		EntityRendererRegistry.register(TheMoonEntities.ASTEROID, AsteroidRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ASTEROID, AsteroidModel::createBodyLayer);

		DimensionRenderingRegistry.registerSkyRenderer(TheMoonDimensionTypes.MOON_LEVEL, new MoonSkyRenderer());
		DimensionRenderingRegistry.registerSkyRenderer(TheMoonDimensionTypes.EXOSPHERE_LEVEL, new ExosphereSkyRenderer());

		ParticleFactoryRegistry.getInstance().register(TheMoonParticleTypes.MOON_DUST, MoonDustParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(TheMoonParticleTypes.FOOTSTEP, FootstepParticle.Provider::new);
    }
}
