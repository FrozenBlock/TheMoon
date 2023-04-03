package net.frozenblock.themoon.tag;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class TheMoonBiomeTags {
	public static final TagKey<Biome> FALLING_ASTEROIDS = bind("falling_asteroids");

	private static TagKey<Biome> bind(String path) {
		return TagKey.create(Registries.BIOME, TheMoonSharedConstants.id(path));
	}
}
