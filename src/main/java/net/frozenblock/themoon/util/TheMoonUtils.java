package net.frozenblock.themoon.util;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonNoiseGeneratorSettings;
import net.frozenblock.themoon.world.generation.biomesource.TheMoonBiomeSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

public class TheMoonUtils {

	public static boolean isStableMoon(LevelStem levelStem) {
		NoiseBasedChunkGenerator noiseBasedChunkGenerator;
		ChunkGenerator chunkGenerator;
		return levelStem.type().is(TheMoonDimensionTypes.MOON) && (chunkGenerator = levelStem.generator()) instanceof NoiseBasedChunkGenerator && (noiseBasedChunkGenerator = (NoiseBasedChunkGenerator)chunkGenerator).stable(TheMoonNoiseGeneratorSettings.MOON) && noiseBasedChunkGenerator.getBiomeSource() instanceof TheMoonBiomeSource;
	}

	public static boolean isOuterSpace(Level level) {
		ResourceKey<DimensionType> dimensionType = level.dimensionTypeId();
		return dimensionType == TheMoonDimensionTypes.MOON;
	}

}
