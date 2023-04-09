package net.frozenblock.themoon.world.generation.features;

import com.mojang.serialization.Codec;
import net.frozenblock.themoon.world.generation.features.config.CraterFeatureConfiguration;
import net.frozenblock.themoon.world.generation.saved.crater.SavedCraterManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import java.util.function.Consumer;

public class CraterFeature extends Feature<CraterFeatureConfiguration> {
	private static final BlockState placeState = Blocks.AIR.defaultBlockState();

	public CraterFeature(Codec<CraterFeatureConfiguration> codec) {
		super(codec);
	}

	public boolean place(FeaturePlaceContext<CraterFeatureConfiguration> context) {
		WorldGenLevel worldGenLevel = context.level();
		BlockPos blockPos = worldGenLevel.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, context.origin()).below();
		RandomSource randomSource = context.random();
		CraterFeatureConfiguration craterFeatureConfiguration = context.config();
		int width = craterFeatureConfiguration.radius().sample(randomSource);
		int depth = craterFeatureConfiguration.depth().sample(randomSource);
		if (depth > width) {
			return false;
		} else {
			int k = (depth * depth + width * width) / (2 * depth);
			BlockPos blockPos2 = blockPos.above(k - depth);
			BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
			Consumer<LevelAccessor> consumer = (levelAccessor) -> {
				double radius = (double)(k * k);
				int x = blockPos.getX();
				int y = blockPos.getY();
				int z = blockPos.getZ();
				for(int offsetY = -depth; offsetY <= k; ++offsetY) {
					boolean bl = false;
					for(int offsetX = -k; offsetX <= k; ++offsetX) {
						for(int offsetZ = -k; offsetZ <= k; ++offsetZ) {
							mutableBlockPos.set(x + offsetX, y + offsetY, z + offsetZ);
							if (mutableBlockPos.distSqr(blockPos2) < radius) {
								bl = true;
								levelAccessor.setBlock(mutableBlockPos, placeState, 2);
							}
						}
					}
					if (!bl && offsetY > 0) {
						break;
					}
				}
			};

			if (k < 15) {
				consumer.accept(worldGenLevel);
			} else {
				ServerLevel serverLevel = worldGenLevel.getLevel();
				serverLevel.getServer().execute(() -> consumer.accept(serverLevel));
			}
			return true;
		}
	}


	/*
	@Override
	public boolean place(FeaturePlaceContext<CraterFeatureConfiguration> context) {
		WorldGenLevel worldGenLevel = context.level();
		BlockPos blockPos = worldGenLevel.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, context.origin()).below();
		RandomSource randomSource = context.random();
		CraterFeatureConfiguration craterFeatureConfiguration = context.config();
		int width = craterFeatureConfiguration.radius().sample(randomSource);
		int depth = craterFeatureConfiguration.depth().sample(randomSource);
		if (depth > width) {
			return false;
		} else {
			int k = (depth * depth + width * width) / (2 * depth);
			BlockPos blockPos2 = blockPos.above(k - depth);
			BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
			ServerLevel serverLevel = worldGenLevel.getLevel();
			SavedCraterManager savedCraterManager = SavedCraterManager.getSavedCraterManager(serverLevel);

			double radius = k * k;
			int x = blockPos.getX();
			int y = blockPos.getY();
			int z = blockPos.getZ();
			for(int offsetY = -depth; offsetY <= k; ++offsetY) {
				for(int offsetX = -k; offsetX <= k; ++offsetX) {
					for(int offsetZ = -k; offsetZ <= k; ++offsetZ) {
						mutableBlockPos.set(x + offsetX, y + offsetY, z + offsetZ);
						if (mutableBlockPos.distSqr(blockPos2) < radius) {
							if (!savedCraterManager.hasCraterAtSamePosAndChunk(mutableBlockPos.immutable(), blockPos)) {
								savedCraterManager.addCrater(new SavedCraterManager.SavedCrater(mutableBlockPos.immutable(), depth, k, blockPos, blockPos2));
								}
							}
						}
					}
				}
			}
			return true;
		}
	 */

}
