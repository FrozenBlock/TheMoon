package net.frozenblock.themoon.registry;

import com.mojang.serialization.Codec;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.generation.biomesource.TheMoonBiomeSource;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeSource;

public class TheMoonBiomeSources {

	public static final ResourceKey<Codec<? extends BiomeSource>> THE_MOON_BIOME_SOURCE = register("the_moon");

	public static void register() {
		TheMoonSharedConstants.logMod("Registering Biome Sources for", TheMoonSharedConstants.UNSTABLE_LOGGING);

		Registry.register(BuiltInRegistries.BIOME_SOURCE, THE_MOON_BIOME_SOURCE, TheMoonBiomeSource.CODEC);
	}

	private static ResourceKey<Codec<? extends BiomeSource>> register(String key) {
		return ResourceKey.create(Registries.BIOME_SOURCE, TheMoonSharedConstants.id(key));
	}
}
