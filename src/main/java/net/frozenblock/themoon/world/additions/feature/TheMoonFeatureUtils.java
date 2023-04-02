package net.frozenblock.themoon.world.additions.feature;

import net.frozenblock.lib.worldgen.feature.api.FrozenConfiguredFeature;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public final class TheMoonFeatureUtils {
	private TheMoonFeatureUtils() {
		throw new UnsupportedOperationException("TheMoonFeatureUtils contains only static declarations.");
	}

	public static FrozenConfiguredFeature<NoneFeatureConfiguration, ConfiguredFeature<NoneFeatureConfiguration, ?>> register(String id, Feature<NoneFeatureConfiguration> feature) {
		return register(id, feature, FeatureConfiguration.NONE);
	}

	public static <FC extends FeatureConfiguration, F extends Feature<FC>> FrozenConfiguredFeature<FC, ConfiguredFeature<FC, ?>> register(@NotNull String id, F feature, @NotNull FC config) {
		var key = TheMoonSharedConstants.id(id);
		FrozenConfiguredFeature<FC, ConfiguredFeature<FC, ?>> frozen = new FrozenConfiguredFeature<>(key);
		frozen.makeAndSetHolder(feature, config);
		return frozen;
	}

	public static <FC extends FeatureConfiguration, F extends Feature<FC>> FrozenConfiguredFeature<FC, ConfiguredFeature<FC, ?>> register(@NotNull String id) {
		var key = TheMoonSharedConstants.id(id);
		return new FrozenConfiguredFeature<>(key);
	}
}
