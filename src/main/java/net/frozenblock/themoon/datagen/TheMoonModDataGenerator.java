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
import net.frozenblock.themoon.registry.TheMoonNoiseGeneratorSettings;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class TheMoonModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		final FabricDataGenerator.Pack pack = dataGenerator.createPack();
		pack.addProvider(TheMoonRegistryProvider::new);
		pack.addProvider(TheMoonBiomeTagProvider::new);
		pack.addProvider(TheMoonBlockTagProvider::new);
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
		registryBuilder.add(Registries.NOISE_SETTINGS, TheMoonNoiseGeneratorSettings::bootstrap);
		//registryBuilder.add(Registries.PROCESSOR_LIST, RegisterStructures::bootstrapProcessor);
		//registryBuilder.add(Registries.TEMPLATE_POOL, RegisterStructures::bootstrapTemplatePool);
		//registryBuilder.add(Registries.STRUCTURE, RegisterStructures::bootstrap);
		//registryBuilder.add(Registries.STRUCTURE_SET, RegisterStructures::bootstrapStructureSet);
	}

	private static class TheMoonBiomeTagProvider extends FrozenBiomeTagProvider {

		public TheMoonBiomeTagProvider(FabricDataOutput output, CompletableFuture registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {

		}
	}

	private static final class TheMoonBlockTagProvider extends FabricTagProvider.BlockTagProvider {
		public TheMoonBlockTagProvider(FabricDataOutput output, CompletableFuture completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {

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
