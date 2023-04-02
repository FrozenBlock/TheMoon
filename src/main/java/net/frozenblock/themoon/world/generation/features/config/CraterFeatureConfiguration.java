package net.frozenblock.themoon.world.generation.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CraterFeatureConfiguration(IntProvider radius, IntProvider depth) implements FeatureConfiguration {

	public static final Codec<CraterFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			(IntProvider.CODEC.fieldOf("radius")).forGetter(CraterFeatureConfiguration::radius),
			(IntProvider.CODEC.fieldOf("depth")).forGetter(CraterFeatureConfiguration::depth)
	).apply(instance, CraterFeatureConfiguration::new));

}
