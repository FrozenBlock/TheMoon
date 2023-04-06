package net.frozenblock.themoon;

import java.util.ArrayList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.mobcategory.api.entrypoint.FrozenMobCategoryEntrypoint;
import net.frozenblock.lib.mobcategory.impl.FrozenMobCategory;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.entity.data.TheMoonEntityDataSerializers;
import net.frozenblock.themoon.entity.spawn.AsteroidBelts;
import net.frozenblock.themoon.entity.spawn.AsteroidSpawner;
import net.frozenblock.themoon.mod_compat.TheMoonModIntegrations;
import net.frozenblock.themoon.registry.TheMoonBiomeSources;
import net.frozenblock.themoon.registry.TheMoonBlocks;
import net.frozenblock.themoon.registry.TheMoonChunkGenerators;
import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.registry.TheMoonFeatures;
import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;

public class TheMoonMod implements ModInitializer, FrozenMobCategoryEntrypoint {

	@Override
	public void onInitialize() {
		TheMoonSharedConstants.startMeasuring(this);
		applyDataFixes(TheMoonSharedConstants.MOD_CONTAINER);

		TheMoonBlocks.register();
		TheMoonEntities.init();
		TheMoonFeatures.register();
		TheMoonBiomeSources.register();
		TheMoonModIntegrations.init();
		TheMoonEntityDataSerializers.init();
		TheMoonParticleTypes.registerParticles();
		TheMoonChunkGenerators.register();

		GravityCalculator.register(TheMoonDimensionTypes.MOON, new GravityCalculator.GravityBelt(-64, true, 128, false, ((entity, y) -> {
			return 0.1;
		})));

		GravityCalculator.register(TheMoonDimensionTypes.MOON, new GravityCalculator.GravityBelt(128, false, 256, false, ((entity, y) -> {
			double progress = (y - 192) / 192;
			return Math.max(Mth.lerp(progress, 0.1, 0), 0.05);
		})));

		GravityCalculator.register(TheMoonDimensionTypes.MOON, new GravityCalculator.GravityBelt(256, false, 320, true, ((entity, y) -> {
			double progress = (y - 192) / 192;
			return Math.max(Mth.lerp(progress, 0.1, 0), 0.05);
		})));

		GravityCalculator.register(TheMoonDimensionTypes.MOON, new GravityCalculator.GravityBelt(320, false, Double.MAX_VALUE, false, ((entity, y) -> {
			double progress = Math.min(Math.min((y - 320), 64) / 64, 1);
			return -Mth.lerp(progress, 0.05, 0.5);
		})));

		AsteroidBelts.register(TheMoonDimensionTypes.MOON, new AsteroidBelts.AsteroidBelt(256, 382, 272, 320));

		GravityCalculator.register(TheMoonDimensionTypes.EXOSPHERE, new GravityCalculator.GravityBelt(-64, true, 999, false, ((entity, y) -> {
			return 0.1;
		})));

		AsteroidBelts.register(TheMoonDimensionTypes.EXOSPHERE, new AsteroidBelts.AsteroidBelt(0, 256, 4, 200));

		AsteroidSpawner.registerSpawnRules(TheMoonDimensionTypes.EXOSPHERE, new AsteroidSpawner.AsteroidSpawnRules(500, 20, 0.9F, 0.05F, 500));

		ServerTickEvents.START_WORLD_TICK.register((serverLevel) -> {
			AsteroidSpawner.spawn(serverLevel, true);
			AsteroidSpawner.spawnFalling(serverLevel, true);
		});

		TheMoonSharedConstants.stopMeasuring(this);
	}

	private static void applyDataFixes(final @NotNull ModContainer mod) {
		TheMoonSharedConstants.log("Applying DataFixes for The Moon with Data Version " + TheMoonSharedConstants.DATA_VERSION, true);

		var builder = new QuiltDataFixerBuilder(TheMoonSharedConstants.DATA_VERSION);
		builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);

		QuiltDataFixes.buildAndRegisterFixer(mod, builder);
		TheMoonSharedConstants.log("DataFixes for The Moon have been applied", true);
	}

	@Override
	public void newCategories(ArrayList<FrozenMobCategory> context) {
		context.add(FrozenMobCategoryEntrypoint.createCategory(TheMoonSharedConstants.id("asteroids"), 70, true, false, 128));
	}
}
