package net.frozenblock.themoon.world.additions.feature;

import net.frozenblock.lib.worldgen.feature.api.FrozenPlacedFeature;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import java.util.List;

public final class TheMoonPlacedFeatures {
	private TheMoonPlacedFeatures() {
		throw new UnsupportedOperationException("TheMoonPlacedFeatures contains only static declarations.");
	}

	public static final FrozenPlacedFeature CRATER_SMALL = TheMoonPlacementUtils.register("crater_small");
	public static final FrozenPlacedFeature CRATER_LARGE = TheMoonPlacementUtils.register("crater_large");
	public static final FrozenPlacedFeature CRATER_MEGA = TheMoonPlacementUtils.register("crater_mega");

	public static final FrozenPlacedFeature MOON_ORE_COAL_UPPER = TheMoonPlacementUtils.register("moon_ore_coal_upper");
	public static final FrozenPlacedFeature MOON_ORE_COAL_LOWER = TheMoonPlacementUtils.register("moon_ore_coal_lower");
	public static final FrozenPlacedFeature MOON_ORE_IRON = TheMoonPlacementUtils.register("moon_ore_iron");
	public static final FrozenPlacedFeature MOON_ORE_IRON_MIDDLE = TheMoonPlacementUtils.register("moon_ore_iron_middle");
	public static final FrozenPlacedFeature MOON_ORE_IRON_SMALL = TheMoonPlacementUtils.register("moon_ore_iron_small");
	public static final FrozenPlacedFeature MOON_ORE_GOLD_EXTRA = TheMoonPlacementUtils.register("moon_ore_gold_extra");
	public static final FrozenPlacedFeature MOON_ORE_GOLD = TheMoonPlacementUtils.register("moon_ore_gold");
	public static final FrozenPlacedFeature MOON_ORE_GOLD_LOWER = TheMoonPlacementUtils.register("moon_ore_gold_lower");
	public static final FrozenPlacedFeature MOON_ORE_REDSTONE = TheMoonPlacementUtils.register("moon_ore_redstone");
	public static final FrozenPlacedFeature MOON_ORE_REDSTONE_LOWER = TheMoonPlacementUtils.register("moon_ore_redstone_lower");
	public static final FrozenPlacedFeature MOON_ORE_DIAMOND = TheMoonPlacementUtils.register("moon_ore_diamond");
	public static final FrozenPlacedFeature MOON_ORE_DIAMOND_LARGE = TheMoonPlacementUtils.register("moon_ore_diamond_large");
	public static final FrozenPlacedFeature MOON_ORE_DIAMOND_BURIED = TheMoonPlacementUtils.register("moon_ore_diamond_buried");
	public static final FrozenPlacedFeature MOON_ORE_LAPIS = TheMoonPlacementUtils.register("moon_ore_copper_lapis");
	public static final FrozenPlacedFeature MOON_ORE_LAPIS_BURIED = TheMoonPlacementUtils.register("moon_ore_lapis_buried");
	public static final FrozenPlacedFeature MOON_ORE_EMERALD = TheMoonPlacementUtils.register("moon_ore_emerald");
	public static final FrozenPlacedFeature MOON_ORE_COPPER = TheMoonPlacementUtils.register("moon_ore_copper");
	public static final FrozenPlacedFeature MOON_ORE_COPPER_LARGE = TheMoonPlacementUtils.register("moon_ore_copper_large");

	public static void registerPlacedFeatures(BootstapContext<PlacedFeature> entries) {
		var configuredFeatures = entries.lookup(Registries.CONFIGURED_FEATURE);
		var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);

		TheMoonSharedConstants.logMod("Registering TheMoonPlacedFeatures for", true);

		CRATER_SMALL.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.CRATER_SMALL.getKey()),
				RarityFilter.onAverageOnceEvery(4),
				InSquarePlacement.spread(),
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
				BiomeFilter.biome()
		);
		CRATER_LARGE.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.CRATER_LARGE.getKey()),
				RarityFilter.onAverageOnceEvery(12),
				InSquarePlacement.spread(),
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
				BiomeFilter.biome()
		);
		CRATER_MEGA.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.CRATER_MEGA.getKey()),
				RarityFilter.onAverageOnceEvery(256),
				InSquarePlacement.spread(),
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
				BiomeFilter.biome()
		);

		MOON_ORE_COAL_UPPER.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_COAL.getKey()),
			commonOrePlacement(1, HeightRangePlacement.uniform(VerticalAnchor.absolute(80), VerticalAnchor.top()))
		);
		MOON_ORE_COAL_LOWER.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_COAL_BURIED.getKey()),
			commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.absolute(20), VerticalAnchor.absolute(96)))
		);
		MOON_ORE_IRON.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_IRON.getKey()),
			commonOrePlacement(15, HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(208)))
		);
		MOON_ORE_IRON_MIDDLE.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_IRON.getKey()),
			commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(30)))
		);
		MOON_ORE_IRON_SMALL.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_IRON_SMALL.getKey()),
			commonOrePlacement(5, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(40)))
		);
		MOON_ORE_GOLD_EXTRA.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_GOLD.getKey()),
			commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(16), VerticalAnchor.absolute(208)))
		);
		MOON_ORE_GOLD.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_GOLD_BURIED.getKey()),
			commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24)))
		);
		MOON_ORE_GOLD_LOWER.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_GOLD_BURIED.getKey()),
			orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(12)))
		);
		MOON_ORE_REDSTONE.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_REDSTONE.getKey()),
			commonOrePlacement(1, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(80)))
		);
		MOON_ORE_REDSTONE_LOWER.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_REDSTONE.getKey()),
			commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)))
		);
		MOON_ORE_DIAMOND.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_DIAMOND_SMALL.getKey()),
			orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-24), VerticalAnchor.aboveBottom(40)))
		);
		MOON_ORE_DIAMOND_LARGE.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_DIAMOND_LARGE.getKey()),
			orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-24), VerticalAnchor.aboveBottom(20)))
		);
		MOON_ORE_DIAMOND_BURIED.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_DIAMOND_BURIED.getKey()),
			orePlacement(CountPlacement.of(UniformInt.of(0, 2)), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-24), VerticalAnchor.absolute(128)))
		);
		MOON_ORE_LAPIS.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_LAPIS.getKey()),
			commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.absolute(2), VerticalAnchor.absolute(16)))
		);
		MOON_ORE_LAPIS_BURIED.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_LAPIS_BURIED.getKey()),
			commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(82)))
		);
		MOON_ORE_EMERALD.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_EMERALD.getKey()),
			commonOrePlacement(1, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(256)))
		);
		MOON_ORE_COPPER.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_COPPER_SMALL.getKey()),
			commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(208)))
		);
		MOON_ORE_COPPER_LARGE.makeAndSetHolder(configuredFeatures.getOrThrow(TheMoonConfiguredFeatures.MOON_ORE_COPPER_LARGE.getKey()),
			commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-16), VerticalAnchor.absolute(38)))
		);
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier firstModifier, PlacementModifier secondModifier) {
		return List.of(firstModifier, InSquarePlacement.spread(), secondModifier, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
		return orePlacement(CountPlacement.of(count), heightRange);
	}

	private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
		return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
	}
}
