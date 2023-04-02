package net.frozenblock.themoon.world.additions.feature;

import net.frozenblock.lib.worldgen.feature.api.FrozenPlacedFeature;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public final class TheMoonPlacedFeatures {
	private TheMoonPlacedFeatures() {
		throw new UnsupportedOperationException("TheMoonPlacedFeatures contains only static declarations.");
	}

	public static final FrozenPlacedFeature CRATER_SMALL = TheMoonPlacementUtils.register("crater_small");

	public static final FrozenPlacedFeature CRATER_LARGE = TheMoonPlacementUtils.register("crater_large");

	public static final FrozenPlacedFeature CRATER_MEGA = TheMoonPlacementUtils.register("crater_mega");

	public static void registerPlacedFeatures(BootstapContext<PlacedFeature> entries) {
		var configuredFeatures = entries.lookup(Registries.CONFIGURED_FEATURE);
		var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);

		TheMoonSharedConstants.logMod("Registering TheMoonPlacedFeatures for", true);

		CRATER_SMALL.makeAndSetHolder(TheMoonConfiguredFeatures.CRATER_SMALL.getHolder(),
				RarityFilter.onAverageOnceEvery(4),
				InSquarePlacement.spread(),
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
				BiomeFilter.biome()
		);

		CRATER_LARGE.makeAndSetHolder(TheMoonConfiguredFeatures.CRATER_LARGE.getHolder(),
				RarityFilter.onAverageOnceEvery(12),
				InSquarePlacement.spread(),
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
				BiomeFilter.biome()
		);

		CRATER_MEGA.makeAndSetHolder(TheMoonConfiguredFeatures.CRATER_MEGA.getHolder(),
				RarityFilter.onAverageOnceEvery(256),
				InSquarePlacement.spread(),
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
				BiomeFilter.biome()
		);
	}
}
