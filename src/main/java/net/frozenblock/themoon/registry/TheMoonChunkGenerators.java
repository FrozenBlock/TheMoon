package net.frozenblock.themoon.registry;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.generation.chunkgenerator.TheExosphereLevelSource;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class TheMoonChunkGenerators {

	public static void register() {
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, TheMoonSharedConstants.id("the_exosphere"), TheExosphereLevelSource.CODEC);
	}

}
