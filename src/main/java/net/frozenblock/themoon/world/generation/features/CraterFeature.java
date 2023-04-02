package net.frozenblock.themoon.world.generation.features;

import com.mojang.serialization.Codec;
import net.frozenblock.themoon.world.generation.features.config.CraterFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import java.util.function.Consumer;

public class CraterFeature extends Feature<CraterFeatureConfiguration> {
	public CraterFeature(Codec<CraterFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<CraterFeatureConfiguration> context) {
		WorldGenLevel worldGenLevel = context.level();
		BlockPos blockPos = worldGenLevel.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, context.origin()).below();
		RandomSource randomSource = context.random();
		CraterFeatureConfiguration craterFeatureConfiguration = context.config();
		int i = craterFeatureConfiguration.radius().sample(randomSource);
		int j = craterFeatureConfiguration.depth().sample(randomSource);
		if (j > i) {
			return false;
		}
		int a = (j * j + i * i) / (2 * j);
		BlockPos blockPos2 = blockPos.above(a - j);
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
		Consumer<LevelAccessor> consumer = levelAccessor -> {
			for (int k = -j; k <= a; ++k) {
				boolean bl = false;
				for (int l = -k; l <= k; ++l) {
					for (int m = -k; m <= k; ++m) {
						mutableBlockPos.setWithOffset(blockPos, l, k, m);
						if (!(mutableBlockPos.distSqr(blockPos2) < (double)(k * k)) || levelAccessor.getBlockState(mutableBlockPos).isAir()) continue;
						bl = true;
						levelAccessor.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 3);
					}
				}
				if (!bl && k > 0) break;
			}
		};
		if (a < 15) {
			consumer.accept(worldGenLevel);
		} else {
			ServerLevel serverLevel = worldGenLevel.getLevel();
			serverLevel.getServer().execute(() -> consumer.accept(serverLevel));
		}
		return true;
	}
}
