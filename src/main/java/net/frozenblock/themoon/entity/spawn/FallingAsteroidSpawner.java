package net.frozenblock.themoon.entity.spawn;

import java.util.ArrayList;
import java.util.List;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.tag.TheMoonBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;

public class FallingAsteroidSpawner {

	public static void spawn(ServerLevel level, boolean spawnBypass) {
		List<ServerPlayer> players = level.players();
		List<BlockPos> poses = new ArrayList<>();
		for (ServerPlayer player : players) {
			poses.add(player.blockPosition());
		}
		RandomSource randomSource = level.getRandom();
		double levelHeight = level.getLogicalHeight();
		for (BlockPos blockPos : poses) {
			if (level.getRandom().nextFloat() < 0.05F) {
				BlockPos pos = new BlockPos(
						blockPos.getX() + randomSource.nextInt(-64, 64),
						0,
						blockPos.getZ() + randomSource.nextInt(-64, 64)
				);
				if (level.isLoaded(pos)) {
					Holder<Biome> biome = level.getBiome(pos);
					if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
						Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
						asteroid.setPos(pos.getX(), levelHeight * 0.5, pos.getZ());
						asteroid.falling = randomSource.nextBoolean();
						if (asteroid.falling) {
							asteroid.setDeltaMovement(randomSource.nextDouble() * 10 * posOrNeg(randomSource), 0, randomSource.nextDouble() * 10 * posOrNeg(randomSource));
						} else {
							asteroid.setNoGravity(true);
							asteroid.setDeltaMovement(randomSource.nextDouble() * 5 * posOrNeg(randomSource), 0, randomSource.nextDouble() * 5 * posOrNeg(randomSource));
						}
						level.addFreshEntity(asteroid);
					}
				}
			}
		}
	}

	private static double posOrNeg(RandomSource randomSource) {
		return randomSource.nextBoolean() ? -1 : 1;
	}

}
