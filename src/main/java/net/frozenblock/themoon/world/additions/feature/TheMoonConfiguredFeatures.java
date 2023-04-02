package net.frozenblock.themoon.world.additions.feature;

import net.frozenblock.lib.worldgen.feature.api.FrozenConfiguredFeature;
import net.frozenblock.themoon.registry.TheMoonFeatures;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.generation.features.config.CraterFeatureConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public final class TheMoonConfiguredFeatures {
	private TheMoonConfiguredFeatures() {
		throw new UnsupportedOperationException("TheMoonConfiguredFeatures contains only static declarations.");
	}

	public static final FrozenConfiguredFeature<CraterFeatureConfiguration, ConfiguredFeature<CraterFeatureConfiguration, ?>> CRATER_SMALL = TheMoonFeatureUtils.register("crater_small");

	public static final FrozenConfiguredFeature<CraterFeatureConfiguration, ConfiguredFeature<CraterFeatureConfiguration, ?>> CRATER_LARGE = TheMoonFeatureUtils.register("crater_large");

	public static final FrozenConfiguredFeature<CraterFeatureConfiguration, ConfiguredFeature<CraterFeatureConfiguration, ?>> CRATER_MEGA = TheMoonFeatureUtils.register("crater_mega");

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
	}
}
