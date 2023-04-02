package net.frozenblock.themoon.world.generation.biomesource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.frozenblock.themoon.registry.TheMoonBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public class TheMoonBiomeSource extends BiomeSource {

	public static final Codec<TheMoonBiomeSource> CODEC = RecordCodecBuilder.create(
			(RecordCodecBuilder.Instance<TheMoonBiomeSource> instance) -> instance.group(
					RegistryOps.retrieveElement(TheMoonBiomes.LUNAR_WASTELANDS)
			).apply(instance, instance.stable(TheMoonBiomeSource::new)));

	private final Holder<Biome> moon;

	public static TheMoonBiomeSource create(HolderGetter<Biome> holderGetter) {
		return new TheMoonBiomeSource(holderGetter.getOrThrow(TheMoonBiomes.LUNAR_WASTELANDS));
	}

	private TheMoonBiomeSource(Holder<Biome> holder) {
		this.moon = holder;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return Stream.of(this.moon);
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int i, int j, int k, Climate.Sampler sampler) {
		return this.moon;
	}
}
