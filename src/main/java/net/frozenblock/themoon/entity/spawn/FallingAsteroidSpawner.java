package net.frozenblock.themoon.entity.spawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.tag.TheMoonBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class FallingAsteroidSpawner {
	private static final Map<Level, List<Asteroid>> ASTEROIDS = new HashMap<>();

	public static void add(Asteroid asteroid) {
		if (ASTEROIDS.containsKey(asteroid.level)) {
			List<Asteroid> asteroids = ASTEROIDS.get(asteroid.level);
			if (!asteroids.contains(asteroid)) {
				ASTEROIDS.get(asteroid.level).add(asteroid);
			}
		} else {
			List<Asteroid> asteroids = new ArrayList<>();
			asteroids.add(asteroid);
			ASTEROIDS.put(asteroid.level, asteroids);
		}
	}

	public static List<Asteroid> getAsteroids(Level level) {
		if (ASTEROIDS.containsKey(level)) {
			return ASTEROIDS.get(level);
		}
		return new ArrayList<>();
	}

	public static int getFallingAsteroids(Level level) {
		int count = 0;
		for (Asteroid asteroid : getAsteroids(level)) {
			if (asteroid.getState() == Asteroid.State.FALLING) {
				count += 1;
			}
		}
		return count;
	}

	public static int getNoGravAsteroids(Level level) {
		int count = 0;
		for (Asteroid asteroid : getAsteroids(level)) {
			if (asteroid.getState() == Asteroid.State.NO_GRAV) {
				count += 1;
			}
		}
		return count;
	}

	public static int getIdleAsteroids(Level level) {
		int count = 0;
		for (Asteroid asteroid : getAsteroids(level)) {
			if (asteroid.getState() == Asteroid.State.IDLE) {
				count += 1;
			}
		}
		return count;
	}

	public static void spawn(ServerLevel level, boolean spawnBypass) {
		List<ServerPlayer> players = level.players();
		List<BlockPos> poses = new ArrayList<>();
		for (ServerPlayer player : players) {
			poses.add(player.blockPosition());
		}
		RandomSource randomSource = level.getRandom();
		double levelHeight = level.getLogicalHeight();
		for (BlockPos blockPos : poses) {
			if (level.getRandom().nextFloat() < 0.075F) {
				BlockPos pos = new BlockPos(
						blockPos.getX() + randomSource.nextInt(-64, 64),
						0,
						blockPos.getZ() + randomSource.nextInt(-64, 64)
				);
				if (level.isLoaded(pos) && level.isNaturalSpawningAllowed(pos)) {
					Holder<Biome> biome = level.getBiome(pos);
					if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
						Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
						asteroid.setPos(pos.getX(), (levelHeight * 0.5) + randomSource.nextInt(0, 64), pos.getZ());
						boolean falling = randomSource.nextBoolean();
						asteroid.setState(falling ? Asteroid.State.FALLING : Asteroid.State.NO_GRAV);
						if (falling) {
							asteroid.setRemainingFireTicks(10);
							asteroid.setDeltaMovement(randomSource.nextDouble() * 2 * posOrNeg(randomSource), -1, randomSource.nextDouble() * 2 * posOrNeg(randomSource));
							asteroid.setScale((randomSource.nextFloat() * 0.5F) + 0.7F);
						} else {
							asteroid.setScale((randomSource.nextFloat() * 2) + 0.7F);
						}
						level.addFreshEntity(asteroid);
					}
				}
			}
		}
	}

	boolean canSpawnForCategory(MobCategory category, ChunkPos pos) {
		int i = category.getMaxInstancesPerChunk() * this.spawnableChunkCount / MAGIC_NUMBER;
		if (this.mobCategoryCounts.getInt(category) >= i) {
			return false;
		}
		return this.localMobCapCalculator.canSpawn(category, pos);
	}

	private static double posOrNeg(RandomSource randomSource) {
		return randomSource.nextBoolean() ? -1 : 1;
	}

}
