package net.frozenblock.themoon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.mobcategory.api.FrozenMobCategories;
import net.frozenblock.lib.mobcategory.api.entrypoint.FrozenMobCategoryEntrypoint;
import net.frozenblock.lib.mobcategory.impl.FrozenMobCategory;
import net.frozenblock.themoon.mod_compat.TheMoonModIntegrations;
import net.frozenblock.themoon.registry.TheMoonBiomeSources;
import net.frozenblock.themoon.registry.TheMoonBiomes;
import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.registry.TheMoonFeatures;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;
import java.util.ArrayList;

public class TheMoonMod implements ModInitializer, FrozenMobCategoryEntrypoint{

	@Override
	public void onInitialize() {
		TheMoonSharedConstants.startMeasuring(this);
		applyDataFixes(TheMoonSharedConstants.MOD_CONTAINER);

		TheMoonEntities.init();
		TheMoonFeatures.register();
		TheMoonBiomeSources.register();
		TheMoonModIntegrations.init();

		GravityCalculator.register(TheMoonDimensionTypes.MOON, (level, y) -> {
			double gravity = 0.1;
			double levelWidth = level.getMaxBuildHeight() - level.getMinBuildHeight();
			double fixedY = Math.max(levelWidth * 0.6, y);
			return gravity * Math.sin((fixedY * Math.PI) / levelWidth);
		});

		GravityCalculator.register(BuiltinDimensionTypes.OVERWORLD, (level, y) -> {
			double gravity = 1;
			double levelWidth = level.getMaxBuildHeight() - level.getMinBuildHeight();
			double fixedY = Math.max(levelWidth * 0.4, y);
			return gravity * Math.sin((fixedY * Math.PI) / levelWidth);
		});

		//BiomeModifications.addSpawn(BiomeSelectors.includeByKey(TheMoonBiomes.LUNAR_WASTELANDS),
		BiomeModifications.addSpawn(BiomeSelectors.all(),
				FrozenMobCategories.getCategory(TheMoonSharedConstants.MOD_ID, "asteroids"), TheMoonEntities.ASTEROID, 2, 1, 2);

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
		context.add(FrozenMobCategoryEntrypoint.createCategory(TheMoonSharedConstants.id("asteroids"), 100, true, false, 528));
	}
}
