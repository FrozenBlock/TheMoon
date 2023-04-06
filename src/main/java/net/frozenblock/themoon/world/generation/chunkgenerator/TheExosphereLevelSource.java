package net.frozenblock.themoon.world.generation.chunkgenerator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.frozenblock.themoon.registry.TheMoonBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jetbrains.annotations.NotNull;

public class TheExosphereLevelSource extends ChunkGenerator {
	public static final Codec<TheExosphereLevelSource> CODEC = RecordCodecBuilder.create((instance) -> instance.group(RegistryOps.retrieveElement(TheMoonBiomes.THE_EXOSPHERE)).apply(instance, instance.stable(TheExosphereLevelSource::new)));

	public TheExosphereLevelSource(Reference<Biome> reference) {
		super(new FixedBiomeSource(reference));
	}

	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	public void buildSurface(@NotNull WorldGenRegion region, @NotNull StructureManager structureManager, @NotNull RandomState randomState, @NotNull ChunkAccess chunk) {
	}

	public void applyBiomeDecoration(@NotNull WorldGenLevel world, @NotNull ChunkAccess chunk, @NotNull StructureManager structureManager) {

	}

	public CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor executor, @NotNull Blender blender, @NotNull RandomState randomState, @NotNull StructureManager structureManager, @NotNull ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	public int getBaseHeight(int x, int z, @NotNull Types heightmap, @NotNull LevelHeightAccessor world, @NotNull RandomState randomState) {
		return 0;
	}

	public NoiseColumn getBaseColumn(int x, int z, @NotNull LevelHeightAccessor world, @NotNull RandomState randomState) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	public void addDebugScreenInfo(@NotNull List<String> list, @NotNull RandomState randomState, @NotNull BlockPos pos) {
	}

	public void applyCarvers(@NotNull WorldGenRegion chunkRegion, long seed, @NotNull RandomState randomState, @NotNull BiomeManager biomeAccess, @NotNull StructureManager structureManager, @NotNull ChunkAccess chunk, @NotNull Carving generationStep) {
	}

	public void spawnOriginalMobs(@NotNull WorldGenRegion region) {
	}

	public int getMinY() {
		return 0;
	}

	public int getGenDepth() {
		return 64;
	}

	public int getSeaLevel() {
		return 0;
	}

}
