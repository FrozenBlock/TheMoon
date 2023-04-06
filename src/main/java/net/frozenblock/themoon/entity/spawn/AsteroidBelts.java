package net.frozenblock.themoon.entity.spawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public final class AsteroidBelts {
	private AsteroidBelts() {
	}

	private static final Map<ResourceKey<DimensionType>, List<AsteroidBelt>> ASTEROID_BELTS = new HashMap<>();

	public static void register(ResourceKey<DimensionType> dimension, AsteroidBelt asteroidBelt) {
		if (ASTEROID_BELTS.containsKey(dimension)) {
			ASTEROID_BELTS.get(dimension).add(asteroidBelt);
		} else {
			List<AsteroidBelt> belts = new ArrayList<>();
			belts.add(asteroidBelt);
			ASTEROID_BELTS.put(dimension, belts);
		}
	}

	public static List<AsteroidBelt> getAllBelts(Level level) {
		ResourceKey<DimensionType> dimension = level.dimensionTypeId();
		if (ASTEROID_BELTS.containsKey(dimension)) {
			return ASTEROID_BELTS.get(dimension);
		} else {
			return new ArrayList<>();
		}
	}

	public static Optional<AsteroidBelt> getEffectingAsteroidBelt(List<AsteroidBelt> belts, double y) {
		Optional<AsteroidBelt> optionalAsteroidBelt = Optional.empty();
		for (AsteroidBelt belt : belts) {
			if (belt.effectsPosition(y)) {
				optionalAsteroidBelt = Optional.of(belt);
				break;
			}
		}
		return optionalAsteroidBelt;
	}

	public record AsteroidBelt(int minY, int maxY, int minSpawnY, int maxSpawnY) {

		public boolean effectsPosition(double y) {
				return y >= minY && y < maxY;
			}

			public boolean canSpawn(double y) {
				return y >= minSpawnY && y < maxSpawnY;
			}
		}
}
