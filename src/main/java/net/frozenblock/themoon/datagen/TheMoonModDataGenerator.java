package net.frozenblock.themoon.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.frozenblock.lib.datagen.api.FrozenBiomeTagProvider;
import net.frozenblock.themoon.registry.TheMoonBiomes;
import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonEntities;
import net.frozenblock.themoon.tag.TheMoonBiomeTags;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.frozenblock.themoon.tag.TheMoonEntityTags;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

public class TheMoonModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		final FabricDataGenerator.Pack pack = dataGenerator.createPack();
		pack.addProvider(TheMoonRegistryProvider::new);
		pack.addProvider(TheMoonEntityTagProvider::new);
		pack.addProvider(TheMoonBiomeTagProvider::new);
		pack.addProvider(TheMoonBlockTagProvider::new);
		pack.addProvider(TheMoonItemTagProvider::new);
		//pack.addProvider(TheMoonDamageTypeTagProvider::new);
		//pack.addProvider(TheMoonItemTagProvider::new);
		//pack.addProvider(TheMoonEntityTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		TheMoonSharedConstants.logMod("Registering Biomes for", TheMoonSharedConstants.UNSTABLE_LOGGING);

		//registryBuilder.add(Registries.DAMAGE_TYPE, RegisterDamageTypes::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_FEATURE, TheMoonFeatureBootstrap::bootstrapConfigured);
		registryBuilder.add(Registries.PLACED_FEATURE, TheMoonFeatureBootstrap::bootstrapPlaced);
		registryBuilder.add(Registries.BIOME, TheMoonBiomes::bootstrap);
		registryBuilder.add(Registries.DIMENSION_TYPE, TheMoonDimensionTypes::bootstrap);
		registryBuilder.add(Registries.NOISE_SETTINGS, TheMoonFeatureBootstrap::bootstrapNoiseSettings);
		//registryBuilder.add(Registries.PROCESSOR_LIST, RegisterStructures::bootstrapProcessor);
		//registryBuilder.add(Registries.TEMPLATE_POOL, RegisterStructures::bootstrapTemplatePool);
		//registryBuilder.add(Registries.STRUCTURE, RegisterStructures::bootstrap);
		//registryBuilder.add(Registries.STRUCTURE_SET, RegisterStructures::bootstrapStructureSet);
	}

	private static final class TheMoonEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
		public TheMoonEntityTagProvider(FabricDataOutput output, CompletableFuture completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			this.getOrCreateTagBuilder(TheMoonEntityTags.NOT_AFFECTED_BY_GRAVITY)
					.addOptional(TheMoonEntities.ASTEROID.builtInRegistryHolder().key());
		}
	}

	private static class TheMoonBiomeTagProvider extends FrozenBiomeTagProvider {

		public TheMoonBiomeTagProvider(FabricDataOutput output, CompletableFuture registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			this.getOrCreateTagBuilder(TheMoonBiomeTags.FALLING_ASTEROIDS)
					.addOptional(TheMoonBiomes.LUNAR_WASTELANDS);
		}
	}

	private static final class TheMoonBlockTagProvider extends FabricTagProvider.BlockTagProvider {
		public TheMoonBlockTagProvider(FabricDataOutput output, CompletableFuture completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
					.addOptional(TheMoonSharedConstants.id("moon_rock"))
					.addOptional(TheMoonSharedConstants.id("moon_rock_stairs"))
					.addOptional(TheMoonSharedConstants.id("moon_rock_slab"))
					.addOptional(TheMoonSharedConstants.id("moon_rock_wall"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_stairs"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_slab"));

			this.getOrCreateTagBuilder(TheMoonBlockTags.MOON_DUST)
					.addOptional(TheMoonSharedConstants.id("moon_rock"))
					.addOptional(TheMoonSharedConstants.id("moon_rock_stairs"))
					.addOptional(TheMoonSharedConstants.id("moon_rock_slab"))
					.addOptional(TheMoonSharedConstants.id("moon_rock_wall"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_stairs"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_slab"));

			this.getOrCreateTagBuilder(BlockTags.STAIRS)
					.addOptional(TheMoonSharedConstants.id("moon_rock_stairs"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_stairs"));

			this.getOrCreateTagBuilder(BlockTags.SLABS)
					.addOptional(TheMoonSharedConstants.id("moon_rock_slab"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_slab"));

			this.getOrCreateTagBuilder(BlockTags.WALLS)
					.addOptional(TheMoonSharedConstants.id("moon_rock_wall"));

			this.getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE)
					.addOptional(TheMoonSharedConstants.id("moon_rock"));

			this.getOrCreateTagBuilder(BlockTags.DRIPSTONE_REPLACEABLE)
					.addOptional(TheMoonSharedConstants.id("moon_rock"));
		}
	}

	private static final class TheMoonItemTagProvider extends FabricTagProvider.ItemTagProvider {
		public TheMoonItemTagProvider(FabricDataOutput output, CompletableFuture completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			this.getOrCreateTagBuilder(ItemTags.STONE_CRAFTING_MATERIALS)
					.addOptional(TheMoonSharedConstants.id("moon_rock"));

			this.getOrCreateTagBuilder(ItemTags.STAIRS)
					.addOptional(TheMoonSharedConstants.id("moon_rock_stairs"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_stairs"));

			this.getOrCreateTagBuilder(ItemTags.SLABS)
					.addOptional(TheMoonSharedConstants.id("moon_rock_slab"))
					.addOptional(TheMoonSharedConstants.id("polished_moon_rock_slab"));

			this.getOrCreateTagBuilder(ItemTags.WALLS)
					.addOptional(TheMoonSharedConstants.id("moon_rock_wall"));
		}
	}

	private static class TheMoonRegistryProvider extends FabricDynamicRegistryProvider {

		public TheMoonRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(HolderLookup.Provider registries, Entries entries) {
			final var damageTypes = asLookup(entries.getLookup(Registries.DAMAGE_TYPE));

			entries.addAll(damageTypes);

			TheMoonFeatureBootstrap.bootstrap(entries);
		}

		@Override
		public String getName() {
			return "The Moon Dynamic Registries";
		}
	}


	public static <T> HolderLookup.RegistryLookup<T> asLookup(HolderGetter<T> getter) {
		return (HolderLookup.RegistryLookup<T>) getter;
	}
}
