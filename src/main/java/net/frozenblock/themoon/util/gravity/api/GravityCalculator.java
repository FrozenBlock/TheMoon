package net.frozenblock.themoon.util.gravity.api;

import java.util.HashMap;
import java.util.Map;
import net.frozenblock.themoon.tag.TheMoonEntityTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class GravityCalculator {
	private static final Map<ResourceKey<DimensionType>, GravityFunction> LEVEL_GRAVITY_FUNCTIONS = new HashMap<>();

	public static void register(ResourceKey<DimensionType> dimension, GravityFunction function) {
		LEVEL_GRAVITY_FUNCTIONS.put(dimension, function);
	}

	public static double calculateGravity(Level level, double y) {
		ResourceKey<DimensionType> dimensionType = level.dimensionTypeId();
		if (LEVEL_GRAVITY_FUNCTIONS.containsKey(dimensionType)) {
			return LEVEL_GRAVITY_FUNCTIONS.get(dimensionType).get(level, y);
		}
		return 1;
	}

	public static double calculateGravity(Entity entity) {
		ResourceKey<DimensionType> dimensionType = entity.level.dimensionTypeId();
		if (LEVEL_GRAVITY_FUNCTIONS.containsKey(dimensionType) && !entity.getType().is(TheMoonEntityTags.NOT_AFFECTED_BY_GRAVITY)) {
			return LEVEL_GRAVITY_FUNCTIONS.get(dimensionType).get(entity.level, entity.getY());
		}
		return 1;
	}

	@FunctionalInterface
	public interface GravityFunction {
		double get(Level level, double y);
	}
}
