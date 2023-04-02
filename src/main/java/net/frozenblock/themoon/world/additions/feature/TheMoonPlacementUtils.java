package net.frozenblock.themoon.world.additions.feature;

import java.util.List;
import net.frozenblock.lib.worldgen.feature.api.FrozenPlacedFeature;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.jetbrains.annotations.NotNull;

public final class TheMoonPlacementUtils {
	private TheMoonPlacementUtils() {
		throw new UnsupportedOperationException("TheMoonPlacementUtils contains only static declarations.");
	}

	public static <FC extends FeatureConfiguration> FrozenPlacedFeature register(@NotNull String id, Holder<ConfiguredFeature<FC, ?>> configured, @NotNull List<PlacementModifier> modifiers) {
		var key = TheMoonSharedConstants.id(id);
		var keyString = key.toString();
		return new FrozenPlacedFeature(key)
				.makeAndSetHolder((Holder) configured, modifiers);
	}

	public static <FC extends FeatureConfiguration> FrozenPlacedFeature register(@NotNull String id, Holder<ConfiguredFeature<FC, ?>> registryEntry, @NotNull PlacementModifier... modifiers) {
		return register(id, registryEntry, List.of(modifiers));
	}

	public static FrozenPlacedFeature register(@NotNull String id) {
		var key = TheMoonSharedConstants.id(id);
		return new FrozenPlacedFeature(key);
	}
}
