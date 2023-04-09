package net.frozenblock.themoon.world.additions.feature;

import java.util.List;
import net.frozenblock.lib.worldgen.feature.api.FrozenConfiguredFeature;
import net.frozenblock.themoon.registry.TheMoonBlocks;
import net.frozenblock.themoon.registry.TheMoonFeatures;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.generation.features.config.CraterFeatureConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public final class TheMoonConfiguredFeatures {
	private TheMoonConfiguredFeatures() {
		throw new UnsupportedOperationException("TheMoonConfiguredFeatures contains only static declarations.");
	}

	public static final FrozenConfiguredFeature<CraterFeatureConfiguration, ConfiguredFeature<CraterFeatureConfiguration, ?>> CRATER_SMALL = TheMoonFeatureUtils.register("crater_small");
	public static final FrozenConfiguredFeature<CraterFeatureConfiguration, ConfiguredFeature<CraterFeatureConfiguration, ?>> CRATER_LARGE = TheMoonFeatureUtils.register("crater_large");
	public static final FrozenConfiguredFeature<CraterFeatureConfiguration, ConfiguredFeature<CraterFeatureConfiguration, ?>> CRATER_MEGA = TheMoonFeatureUtils.register("crater_mega");

	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_COAL = TheMoonFeatureUtils.register("moon_ore_coal");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_COAL_BURIED = TheMoonFeatureUtils.register("moon_ore_coal_buried");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_IRON = TheMoonFeatureUtils.register("moon_ore_iron");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_IRON_SMALL = TheMoonFeatureUtils.register("moon_ore_iron_small");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_GOLD = TheMoonFeatureUtils.register("moon_ore_gold");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_GOLD_BURIED = TheMoonFeatureUtils.register("moon_ore_gold_buried");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_REDSTONE = TheMoonFeatureUtils.register("moon_ore_redstone");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_DIAMOND_SMALL = TheMoonFeatureUtils.register("moon_ore_diamond_small");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_DIAMOND_LARGE = TheMoonFeatureUtils.register("moon_ore_diamond_large");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_DIAMOND_BURIED = TheMoonFeatureUtils.register("moon_ore_diamond_buried");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_LAPIS = TheMoonFeatureUtils.register("moon_ore_lapis");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_LAPIS_BURIED = TheMoonFeatureUtils.register("moon_ore_lapis_buried");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_EMERALD = TheMoonFeatureUtils.register("moon_ore_emerald");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_COPPER_SMALL = TheMoonFeatureUtils.register("moon_ore_copper_small");
	public static final FrozenConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> MOON_ORE_COPPER_LARGE = TheMoonFeatureUtils.register("moon_ore_copper_large");

	public static void registerConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> entries) {
		var configuredFeatures = entries.lookup(Registries.CONFIGURED_FEATURE);
		var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);

		TheMoonSharedConstants.logMod("Registering TheMoonConfiguredFeatures for", true);

		CRATER_SMALL.makeAndSetHolder(TheMoonFeatures.CRATER,
				new CraterFeatureConfiguration(UniformInt.of(4, 7), UniformInt.of(2, 3)));
		CRATER_LARGE.makeAndSetHolder(TheMoonFeatures.CRATER,
				new CraterFeatureConfiguration(UniformInt.of(12, 15), UniformInt.of(3, 5)));
		CRATER_MEGA.makeAndSetHolder(TheMoonFeatures.CRATER,
				new CraterFeatureConfiguration(UniformInt.of(32, 48), UniformInt.of(8, 16)));

		TagMatchTest moonOreReplaceable = new TagMatchTest(TheMoonBlockTags.MOON_ROCK_ORE_REPLACEABLES);
		List<OreConfiguration.TargetBlockState> moonIronList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_IRON_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonGoldList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_GOLD_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonDiamondList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_DIAMOND_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonLapisList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_LAPIS_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonCopperList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_COPPER_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonCoalList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_COAL_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonRedstoneList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_REDSTONE_ORE.defaultBlockState()));
		List<OreConfiguration.TargetBlockState> moonEmeraldList = List.of(OreConfiguration.target(moonOreReplaceable, TheMoonBlocks.MOON_ROCK_EMERALD_ORE.defaultBlockState()));

		MOON_ORE_COAL.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonCoalList, 2, 0.3F));

		MOON_ORE_COAL_BURIED.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonCoalList, 4, 0.5F));

		MOON_ORE_IRON.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonIronList, 6, 0.3F));

		MOON_ORE_IRON_SMALL.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonCoalList, 3, 0.3F));

		MOON_ORE_GOLD.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonGoldList, 3, 0.3F));

		MOON_ORE_GOLD_BURIED.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonCoalList, 7, 0.7F));

		MOON_ORE_REDSTONE.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonRedstoneList, 8, 0.3F));

		MOON_ORE_DIAMOND_SMALL.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonDiamondList, 2, 0.7F));

		MOON_ORE_DIAMOND_LARGE.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonDiamondList, 6, 0.8F));

		MOON_ORE_DIAMOND_BURIED.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonDiamondList, 4, 1.0F));

		MOON_ORE_LAPIS.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonLapisList, 7, 0.3F));

		MOON_ORE_LAPIS_BURIED.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonLapisList, 7, 1.0F));

		MOON_ORE_EMERALD.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonEmeraldList, 3, 0.3F));

		MOON_ORE_COPPER_SMALL.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonCopperList, 7, 0.3F));

		MOON_ORE_COPPER_LARGE.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(moonCopperList, 14, 0.3F));
	}
}
