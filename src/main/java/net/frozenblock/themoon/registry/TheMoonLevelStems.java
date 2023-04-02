package net.frozenblock.themoon.registry;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;

public class TheMoonLevelStems {

	public static final ResourceKey<LevelStem> MOON = ResourceKey.create(Registries.LEVEL_STEM, TheMoonSharedConstants.id("the_moon"));

}
