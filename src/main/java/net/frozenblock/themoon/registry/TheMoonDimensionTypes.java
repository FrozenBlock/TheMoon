package net.frozenblock.themoon.registry;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;

public class TheMoonDimensionTypes {

	public static final ResourceKey<DimensionType> MOON = register("the_moon");
	public static final ResourceLocation MOON_EFFECTS = TheMoonSharedConstants.id("the_moon");

	private static ResourceKey<DimensionType> register(String name) {
		return ResourceKey.create(Registries.DIMENSION_TYPE, TheMoonSharedConstants.id(name));
	}

}
