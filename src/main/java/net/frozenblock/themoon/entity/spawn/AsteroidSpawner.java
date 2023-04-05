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
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;

public class AsteroidSpawner {
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

	public static void clear(Level level) {
		getAsteroids(level).removeIf(asteroid -> asteroid == null || asteroid.isRemoved());
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

	public static List<BlockPos> getRandomPoses(ServerLevel level) {
		List<BlockPos> blockPoses = new ArrayList<>();
		for (ChunkHolder holder : level.getChunkSource().chunkMap.getChunks()) {
			LevelChunk chunk = holder.getFullChunk();
			if (chunk != null) {
				blockPoses.add(getRandomPosWithin(level, chunk));
			}
		}
		return blockPoses;
	}

	private static BlockPos getRandomPosWithin(Level world, LevelChunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		int i = chunkPos.getMinBlockX() + world.random.nextInt(16);
		int j = chunkPos.getMinBlockZ() + world.random.nextInt(16);
		return new BlockPos(i, 0, j);
	}

	public static void spawnFalling(ServerLevel level, boolean spawnBypass) {
		RandomSource randomSource = level.getRandom();
		double levelHeight = level.getLogicalHeight();
		for (BlockPos pos : getRandomPoses(level)) {
			if (level.getRandom().nextFloat() < 0.075F) {
				if (level.isLoaded(pos) && level.isNaturalSpawningAllowed(pos)) {
					Holder<Biome> biome = level.getBiome(pos);
					if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
						Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
						asteroid.setPos(pos.getX(), (levelHeight * 0.5) + randomSource.nextInt(0, 64), pos.getZ());
						asteroid.setState(Asteroid.State.FALLING);
						if (getFallingAsteroids(level) <= level.players().size()) {
							asteroid.setRemainingFireTicks(10);
							asteroid.setDeltaMovement(randomSource.nextDouble() * 2 * posOrNeg(randomSource), -1, randomSource.nextDouble() * 2 * posOrNeg(randomSource));
							asteroid.setScale((randomSource.nextFloat() * 0.5F) + 0.7F);
							level.addFreshEntity(asteroid);
						}
					}
				}
			}
		}
	}

	public static void spawn(ServerLevel level, boolean spawnBypass) {
		List<ServerPlayer> players = level.players();
		List<BlockPos> poses = new ArrayList<>();
		for (ServerPlayer player : players) {
			poses.add(player.blockPosition());
		}
		RandomSource randomSource = level.getRandom();
		double levelHeight = level.getLogicalHeight();
		for (BlockPos pos : poses) {
			if (level.getRandom().nextFloat() < 0.075F) {
				if (level.isLoaded(pos) && level.isNaturalSpawningAllowed(pos)) {
					Holder<Biome> biome = level.getBiome(pos);
					if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
						Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
						asteroid.setPos(pos.getX(), (levelHeight * 0.5) + randomSource.nextInt(0, 64), pos.getZ());
						if (getNoGravAsteroids(level) < asteroid.getType().getCategory().getMaxInstancesPerChunk()) {
							asteroid.setScale((randomSource.nextFloat() * 2) + 0.7F);
							asteroid.setState(Asteroid.State.NO_GRAV);
							level.addFreshEntity(asteroid);
						}
					}
				}
			}
		}
	}

	private static double posOrNeg(RandomSource randomSource) {
		return randomSource.nextBoolean() ? -1 : 1;
	}

}
