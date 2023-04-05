package net.frozenblock.themoon.tag;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TheMoonBlockTags {
	public static final TagKey<Block> MOON_DUST = bind("moon_dust");

	private static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, TheMoonSharedConstants.id(path));
	}
}
