package net.frozenblock.themoon.datagen;

import java.util.Arrays;
import java.util.List;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.frozenblock.lib.worldgen.feature.api.FrozenConfiguredFeatureUtils;
import net.frozenblock.lib.worldgen.feature.api.FrozenFeatureUtils;
import net.frozenblock.lib.worldgen.feature.api.FrozenPlacementUtils;
import net.frozenblock.themoon.registry.TheMoonNoiseGeneratorSettings;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.additions.feature.TheMoonConfiguredFeatures;
import net.frozenblock.themoon.world.additions.feature.TheMoonPlacedFeatures;
import net.frozenblock.themoon.world.generation.noise.TheMoonDensityFunctions;
import net.frozenblock.themoon.world.generation.noise.TheMoonNoiseData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TheMoonFeatureBootstrap {

	public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> entries) {
		final var configuredFeatures = entries.lookup(Registries.CONFIGURED_FEATURE);
		final var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);

		FrozenFeatureUtils.BOOTSTAP_CONTEXT = (BootstapContext) entries;

		TheMoonConfiguredFeatures.registerConfiguredFeatures(entries);
	}

	public static void bootstrapPlaced(BootstapContext<PlacedFeature> entries) {
		final var configuredFeatures = entries.lookup(Registries.CONFIGURED_FEATURE);
		final var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);

		FrozenFeatureUtils.BOOTSTAP_CONTEXT = (BootstapContext) entries;

		TheMoonPlacedFeatures.registerPlacedFeatures(entries);
	}

	public static void bootstrapNoiseSettings(BootstapContext<NoiseGeneratorSettings> entries) {
		TheMoonNoiseGeneratorSettings.bootstrap(entries);
	}

	public static void bootstrapDensityFunctions(BootstapContext<DensityFunction> entries) {
		TheMoonDensityFunctions.bootstrap(entries);
	}

	public static void bootstrapNoise(BootstapContext<NormalNoise.NoiseParameters> entries) {
		TheMoonNoiseData.bootstrap(entries);
	}

	public static void bootstrap(FabricDynamicRegistryProvider.Entries entries) {
		final var configuredFeatures = asLookup(entries.getLookup(Registries.CONFIGURED_FEATURE));
		final var placedFeatures = asLookup(entries.placedFeatures());
		final var biomes = asLookup(entries.getLookup(Registries.BIOME));
		final var noises = asLookup(entries.getLookup(Registries.NOISE));
		final var processorLists = asLookup(entries.getLookup(Registries.PROCESSOR_LIST));
		final var templatePools = asLookup(entries.getLookup(Registries.TEMPLATE_POOL));
		final var structures = asLookup(entries.getLookup(Registries.STRUCTURE));
		final var structureSets = asLookup(entries.getLookup(Registries.STRUCTURE_SET));
		final var noiseSettings = asLookup(entries.getLookup(Registries.NOISE_SETTINGS));
		final var dimensionTypes = asLookup(entries.getLookup(Registries.DIMENSION_TYPE));
		final var densityFunctions = asLookup(entries.getLookup(Registries.DENSITY_FUNCTION));

		TheMoonSharedConstants.log("Adding finalized configured features to datagen", true);
		entries.addAll(configuredFeatures);
		TheMoonSharedConstants.log("Adding finalized placed features to datagen", true);
		entries.addAll(placedFeatures);
		TheMoonSharedConstants.log("Adding finalized biomes to datagen", true);
		entries.addAll(biomes);
		TheMoonSharedConstants.log("Adding finalized noises to datagen", true);
		entries.addAll(noises);
		TheMoonSharedConstants.log("Adding finalized processor lists to datagen", true);
		entries.addAll(processorLists);
		TheMoonSharedConstants.log("Adding finalized template pools to datagen", true);
		entries.addAll(templatePools);
		TheMoonSharedConstants.log("Adding finalized structures to datagen", true);
		entries.addAll(structures);
		TheMoonSharedConstants.log("Adding finalized structure sets to datagen", true);
		entries.addAll(structureSets);
		TheMoonSharedConstants.log("Adding finalized noise settings to datagen", true);
		entries.addAll(noiseSettings);
		TheMoonSharedConstants.log("Adding finalized dimension types to datagen", true);
		entries.addAll(dimensionTypes);
		TheMoonSharedConstants.log("Adding finalized density functions to datagen", true);
		entries.addAll(densityFunctions);
	}

	/**
	 * @param configuredResourceKey	MUST BE A VANILLA CONFIGURED FEATURE
	 */
	public static Holder<PlacedFeature> register(BootstapContext<PlacedFeature> entries, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredResourceKey, PlacementModifier... modifiers) {
		return register(entries, resourceKey, configuredResourceKey, Arrays.asList(modifiers));
	}

	/**
	 * @param configuredResourceKey	MUST BE A VANILLA CONFIGURED FEATURE
	 */
	public static Holder<PlacedFeature> register(BootstapContext<PlacedFeature> entries, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredResourceKey, List<PlacementModifier> modifiers) {
		return FrozenPlacementUtils.register(entries, resourceKey, entries.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredResourceKey), modifiers);
	}


	public static Holder<PlacedFeature> register(BootstapContext<PlacedFeature> entries, ResourceKey<PlacedFeature> resourceKey, Holder<ConfiguredFeature<?, ?>> configuredHolder, PlacementModifier... modifiers) {
		return register(entries, resourceKey, configuredHolder, Arrays.asList(modifiers));
	}

	private static Holder<PlacedFeature> register(BootstapContext<PlacedFeature> entries, ResourceKey<PlacedFeature> resourceKey, Holder<ConfiguredFeature<?, ?>> configuredHolder, List<PlacementModifier> modifiers) {
		return FrozenPlacementUtils.register(entries, resourceKey, configuredHolder, modifiers);
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<?, ?>> register(BootstapContext<ConfiguredFeature<?, ?>> entries, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, F feature, FC featureConfiguration) {
		return FrozenConfiguredFeatureUtils.register(entries, resourceKey, feature, featureConfiguration);
	}

	public static <T> HolderLookup.RegistryLookup<T> asLookup(HolderGetter<T> getter) {
		return (HolderLookup.RegistryLookup<T>) getter;
	}
}
