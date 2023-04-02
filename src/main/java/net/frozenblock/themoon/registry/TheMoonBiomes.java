package net.frozenblock.themoon.registry;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.additions.feature.TheMoonPlacedFeatures;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public final class TheMoonBiomes {

	public static final ResourceKey<Biome> LUNAR_WASTELANDS = register("lunar_wastelands");

	public static void bootstrap(BootstapContext<Biome> context) {
		TheMoonSharedConstants.logMod("Registering Biomes for", TheMoonSharedConstants.UNSTABLE_LOGGING);

		register(context, TheMoonBiomes.LUNAR_WASTELANDS, TheMoonBiomes.lunarWastelands(context));
	}

	public static Biome lunarWastelands(BootstapContext<Biome> entries) {
		var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);
		var worldCarvers = entries.lookup(Registries.CONFIGURED_CARVER);
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, TheMoonPlacedFeatures.CRATER_MEGA.getHolder());
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, TheMoonPlacedFeatures.CRATER_LARGE.getHolder());
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, TheMoonPlacedFeatures.CRATER_SMALL.getHolder());
		return baseMoonBiome(builder);
	}

	private static Biome baseMoonBiome(BiomeGenerationSettings.Builder builder) {
		net.minecraft.world.level.biome.MobSpawnSettings.Builder mobSpawns = new net.minecraft.world.level.biome.MobSpawnSettings.Builder();
		//.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOON_COW, 8, 1, 5));
		mobSpawns.creatureGenerationProbability(0.07F);
		return new Biome.BiomeBuilder()
				.hasPrecipitation(false)
				.temperature(-0.5F)
				.downfall(0.5F)
				.specialEffects(
						new net.minecraft.world.level.biome.BiomeSpecialEffects.Builder()
								.waterColor(0)
								.waterFogColor(16777215)
								.fogColor(10518688)
								.skyColor(0)
								.foliageColorOverride(16777215)
								.grassColorOverride(6908265)
								.ambientParticle(
										new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.001F)
								)
								.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()
				)
				.mobSpawnSettings(mobSpawns.build())
				.generationSettings(builder.build()).build();
	}

	private static ResourceKey<Biome> register(String name) {
		return ResourceKey.create(Registries.BIOME, TheMoonSharedConstants.id(name));
	}

	private static void register(BootstapContext<Biome> entries, ResourceKey<Biome> key, Biome biome) {
		TheMoonSharedConstants.log("Registering biome " + key.location(), true);
		entries.register(key, biome);
	}
}