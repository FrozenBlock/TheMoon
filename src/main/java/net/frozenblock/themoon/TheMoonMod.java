package net.frozenblock.themoon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.themoon.mod_compat.TheMoonModIntegrations;
import net.frozenblock.themoon.registry.TheMoonBiomeSources;
import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonFeatures;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;

public class TheMoonMod implements ModInitializer {

	@Override
	public void onInitialize() {
		TheMoonSharedConstants.startMeasuring(this);
		applyDataFixes(TheMoonSharedConstants.MOD_CONTAINER);

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
			double fixedY = Math.max(levelWidth * 0.6, y);
			return gravity * Math.sin((fixedY * Math.PI) / levelWidth);
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
}
