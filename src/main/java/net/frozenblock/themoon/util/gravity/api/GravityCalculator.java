package net.frozenblock.themoon.util.gravity.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.entity.spawn.AsteroidBelts;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class GravityCalculator {
	private static final Map<ResourceKey<DimensionType>, List<GravityBelt>> GRAVITY_BELTS = new HashMap<>();

	public static void register(ResourceKey<DimensionType> dimension, GravityBelt gravityBelt) {
		if (GRAVITY_BELTS.containsKey(dimension)) {
			GRAVITY_BELTS.get(dimension).add(gravityBelt);
		} else {
			List<GravityBelt> belts = new ArrayList<>();
			belts.add(gravityBelt);
			GRAVITY_BELTS.put(dimension, belts);
		}
	}

	public static List<GravityBelt> getAllBelts(Level level) {
		ResourceKey<DimensionType> dimension = level.dimensionTypeId();
		if (GRAVITY_BELTS.containsKey(dimension)) {
			return GRAVITY_BELTS.get(dimension);
		} else {
			return new ArrayList<>();
		}
	}

	public static double calculateGravity(Level level, double y) {
		ResourceKey<DimensionType> dimensionType = level.dimensionTypeId();
		if (GRAVITY_BELTS.containsKey(dimensionType)) {
			Optional<GravityBelt> optionalGravityBelt = getEffectingGravityBelt(GRAVITY_BELTS.get(dimensionType), y);
			if (optionalGravityBelt.isPresent()) {
				GravityBelt gravityBelt = optionalGravityBelt.get();
				return gravityBelt.getGravity(null, y);
			}
		}
		return 1;
	}

	public static double calculateGravity(Entity entity) {
		ResourceKey<DimensionType> dimensionType = entity.level.dimensionTypeId();
		if (GRAVITY_BELTS.containsKey(dimensionType)) {
			double y = entity.getY();
			Optional<GravityBelt> optionalGravityBelt = getEffectingGravityBelt(GRAVITY_BELTS.get(dimensionType), y);
			if (optionalGravityBelt.isPresent()) {
				GravityBelt gravityBelt = optionalGravityBelt.get();
				double gravity = gravityBelt.getGravity(entity, y);
				if (entity instanceof Asteroid asteroid) {
					Asteroid.State state = asteroid.getState();
					if (AsteroidBelts.getEffectingAsteroidBelt(AsteroidBelts.getAllBelts(entity.level), entity.getY()).isEmpty()) {
						return state == Asteroid.State.FALLING ? 1 : gravity;
					} else {
						asteroid.isInAsteroidBelt = true;
						if (!Asteroid.FALLING_ASTEROIDS_EFFECTED_BY_ASTEROID_BELTS && state == Asteroid.State.FALLING) {
							return 1;
						}
						return 0;
					}
				}
				return gravity;
			}
		}
		return 1;
	}

	public static Direction getGravityDirection(Entity entity) {
		return calculateGravity(entity) >= 0 ? Direction.DOWN : Direction.UP;
	}

	public static boolean isGravityDown(Entity entity) {
		return getGravityDirection(entity) == Direction.DOWN;
	}

	public static Optional<GravityBelt> getEffectingGravityBelt(List<GravityBelt> belts, double y) {
		Optional<GravityBelt> optionalGravityBelt = Optional.empty();
		for (GravityBelt belt : belts) {
			if (belt.effectsPosition(y)) {
				optionalGravityBelt = Optional.of(belt);
				break;
			}
		}
		return optionalGravityBelt;
	}

	public static class GravityBelt {
		public final double minY;
		public final boolean renderBottom;
		public final double maxY;
		public final boolean renderTop;
		private final GravityFunction function;

		public GravityBelt(double minY, boolean renderBottom, double maxY, boolean renderTop, GravityFunction function) {
			this.minY = minY;
			this.renderBottom = renderBottom;
			this.maxY = maxY;
			this.renderTop = renderTop;
			this.function = function;
		}

		public boolean effectsPosition(double y) {
			return y >= minY && y < maxY;
		}

		protected double getGravity(@Nullable Entity entity, double y) {;
			if (this.effectsPosition(y)) {
				return this.function.get(entity, y);
			}
			return 1;
		}
	}

	@FunctionalInterface
	public interface GravityFunction {
		double get(@Nullable Entity entity, double y);
	}
}
