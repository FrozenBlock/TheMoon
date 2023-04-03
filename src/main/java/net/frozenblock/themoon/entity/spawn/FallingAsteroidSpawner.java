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
		for (BlockPos pos : poses) {
			Holder<Biome> biome = level.getBiome(pos);
			if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
				if (level.getRandom().nextFloat() < 0.05F) {
					Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
					asteroid.setPos(pos.getX(), levelHeight * 1.5, pos.getZ());
					asteroid.setDeltaMovement(randomSource.nextDouble() * posOrNeg(randomSource), -2, randomSource.nextDouble() * posOrNeg(randomSource));
					level.addFreshEntity(asteroid);
				}
			}
		}
	}

	private static double posOrNeg(RandomSource randomSource) {
		return randomSource.nextBoolean() ? -1 : 1;
	}

}
