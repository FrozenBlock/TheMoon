package net.frozenblock.themoon.world.generation.noise;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TheMoonNoises {
	public static final ResourceKey<NormalNoise.NoiseParameters> TEMPERATURE = createKey("temperature");
	public static final ResourceKey<NormalNoise.NoiseParameters> VEGETATION = createKey("vegetation");
	public static final ResourceKey<NormalNoise.NoiseParameters> CONTINENTALNESS = createKey("continentalness");
	public static final ResourceKey<NormalNoise.NoiseParameters> EROSION = createKey("erosion");
	public static final ResourceKey<NormalNoise.NoiseParameters> CRATER = createKey("crater");
	public static final ResourceKey<NormalNoise.NoiseParameters> CRATER_RARENESS = createKey("crater_rareness");
	public static final ResourceKey<NormalNoise.NoiseParameters> CRATER_THICKNESS = createKey("crater_thickness");

	private static ResourceKey<NormalNoise.NoiseParameters> createKey(String key) {
		return ResourceKey.create(Registries.NOISE, TheMoonSharedConstants.id(key));
	}
}
