package net.frozenblock.themoon.entity.spawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.tag.TheMoonBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class AsteroidSpawner {

	private static final Map<ResourceKey<DimensionType>, AsteroidSpawnRules> ASTEROID_SPAWN_RULES = new HashMap<>();

	public static void registerSpawnRules(ResourceKey<DimensionType> dimension, AsteroidSpawnRules asteroidSpawnRules) {
		ASTEROID_SPAWN_RULES.put(dimension, asteroidSpawnRules);
	}

	public static AsteroidSpawnRules getSpawnRules(Level level) {
		ResourceKey<DimensionType> dimension = level.dimensionTypeId();
		if (ASTEROID_SPAWN_RULES.containsKey(dimension)) {
			return ASTEROID_SPAWN_RULES.get(dimension);
		}
		return DEFAULT_SPAWN_RULES;
	}

	private static final AsteroidSpawnRules DEFAULT_SPAWN_RULES = new AsteroidSpawnRules(100, 1, 0.075F, 0.01F, 500);

	public static List<Asteroid> getAsteroids(ServerLevel level) {
		ArrayList<Asteroid> asteroids = new ArrayList<>();
		for (Entity entity : level.entityManager.getEntityGetter().getAll()) {
			if (entity instanceof Asteroid asteroid && !asteroid.isRemoved()) {
				asteroids.add(asteroid);
			}
		}
		return asteroids;
	}

	public static int getFallingAsteroids(ServerLevel level) {
		int count = 0;
		for (Asteroid asteroid : getAsteroids(level)) {
			if (asteroid.getState() == Asteroid.State.FALLING) {
				count += 1;
			}
		}
		return count;
	}

	public static int getNoGravAsteroids(ServerLevel level) {
		int count = 0;
		for (Asteroid asteroid : getAsteroids(level)) {
			if (asteroid.getState() == Asteroid.State.NO_GRAV) {
				count += 1;
			}
		}
		return count;
	}

	public static int getIdleAsteroids(ServerLevel level) {
		int count = 0;
		for (Asteroid asteroid : getAsteroids(level)) {
			if (asteroid.getState() == Asteroid.State.IDLE) {
				count += 1;
			}
		}
		return count;
	}

	public static void spawnFalling(ServerLevel level, boolean spawnBypass) {
		List<ServerPlayer> players = level.players();
		RandomSource randomSource = level.getRandom();
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mutableChunkBlockPos = new BlockPos.MutableBlockPos();
		AsteroidSpawnRules spawnRules = getSpawnRules(level);
		for (ServerPlayer player : players) {
			if (level.getRandom().nextFloat() <= spawnRules.fallingSpawnChance() && getFallingAsteroids(level) < spawnRules.maxFalling()) {
				mutableBlockPos.set(player.blockPosition());
				mutableBlockPos.set(
						mutableBlockPos.getX() + randomSource.nextInt(-128, 128),
						spawnRules.fallingSpawnHeight(),
						mutableBlockPos.getZ() + randomSource.nextInt(-128, 128)
				);
				mutableChunkBlockPos.set(
						mutableBlockPos.getX(),
						0,
						mutableBlockPos.getZ()
				);
				Holder<Biome> biome = level.getBiome(mutableBlockPos);
				if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
					Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
					asteroid.setPos(mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ());
					asteroid.setScale(randomSource.nextFloat() + 0.7F);
					asteroid.setState(Asteroid.State.FALLING);
					if (!asteroid.isPlayerWithin(32) && level.noCollision(asteroid.makeBoundingBox())) {
						asteroid.setDeltaMovement(
								randomSource.nextDouble() * posOrNeg(randomSource) * 0.4,
								-1,
								randomSource.nextDouble() * posOrNeg(randomSource) * 0.4
						);
						level.addFreshEntity(asteroid);
					}
				}
			}
		}
	}

	public static void spawn(ServerLevel level, boolean spawnBypass) {
		RandomSource randomSource = level.getRandom();
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mutableChunkBlockPos = new BlockPos.MutableBlockPos();
		AsteroidSpawnRules spawnRules = getSpawnRules(level);
		for (ServerPlayer player : level.players()) {
			if (level.getRandom().nextFloat() < spawnRules.noGravSpawnChance() && getNoGravAsteroids(level) < spawnRules.maxNoGrav()) {
				Optional<Integer> optionalInteger = getRandomSpawnHeight(level);
				if (optionalInteger.isPresent()) {
					mutableBlockPos.set(player.blockPosition());
					mutableBlockPos.set(
							mutableBlockPos.getX() + randomSource.nextInt(-128, 128),
							optionalInteger.get(),
							mutableBlockPos.getZ() + randomSource.nextInt(-128, 128)
					);
					mutableChunkBlockPos.set(
							mutableBlockPos.getX(),
							0,
							mutableBlockPos.getZ()
					);
					Holder<Biome> biome = level.getBiome(mutableBlockPos);
					if (biome.is(TheMoonBiomeTags.FALLING_ASTEROIDS) || spawnBypass) {
						Asteroid asteroid = new Asteroid(TheMoonEntities.ASTEROID, level);
						asteroid.setPos(mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ());
						asteroid.setScale((randomSource.nextFloat() * 2) + 0.7F);
						if (getNoGravAsteroids(level) < asteroid.getType().getCategory().getMaxInstancesPerChunk() && !asteroid.isPlayerWithin(32) && level.noCollision(asteroid.makeBoundingBox())) {
							asteroid.setState(Asteroid.State.NO_GRAV);
							level.addFreshEntity(asteroid);
						}
					}
				}
			}
		}
	}

	private static Optional<Integer> getRandomSpawnHeight(Level level) {
		Optional<Integer> optionalInteger = Optional.empty();
		AsteroidBelts.AsteroidBelt belt = getWeightedBelt(AsteroidBelts.getAllBelts(level));
		if (belt != null) {
			optionalInteger = Optional.of(level.getRandom().nextInt(belt.minSpawnY(), belt.maxSpawnY()));
		}
		return optionalInteger;
	}

	@Nullable
	private static AsteroidBelts.AsteroidBelt getWeightedBelt(List<AsteroidBelts.AsteroidBelt> belts) {
		double completeWeight = 0;
		for (AsteroidBelts.AsteroidBelt belt : belts) {
			completeWeight += (belt.maxSpawnY() - belt.minSpawnY());
		}
		double r = Math.random() * completeWeight;
		double countWeight = 0;
		for (AsteroidBelts.AsteroidBelt belt : belts) {
			countWeight += (belt.maxSpawnY() - belt.minSpawnY());
			if (countWeight >= r)
				return belt;
		}
		return null;
	}

	private static double posOrNeg(RandomSource randomSource) {
		return randomSource.nextBoolean() ? -1 : 1;
	}

	public record AsteroidSpawnRules(int maxNoGrav, int maxFalling, float noGravSpawnChance, float fallingSpawnChance, int fallingSpawnHeight) {
	}

}
