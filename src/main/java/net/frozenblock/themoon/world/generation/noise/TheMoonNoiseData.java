package net.frozenblock.themoon.world.generation.noise;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TheMoonNoiseData {

	public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> bootstapContext) {
		registerBiomeNoises(bootstapContext, -1, TheMoonNoises.TEMPERATURE, TheMoonNoises.VEGETATION, TheMoonNoises.CONTINENTALNESS, TheMoonNoises.EROSION);
		register(bootstapContext, TheMoonNoises.CRATER, -6, 1.0, 1.0);
		register(bootstapContext, TheMoonNoises.CRATER_RARENESS, -2, 1.0);
		register(bootstapContext, TheMoonNoises.CRATER_THICKNESS, -6, 1.0);
	}

	private static void registerBiomeNoises
		(
			BootstapContext<NormalNoise.NoiseParameters> bootstapContext,
			int i,
			ResourceKey<NormalNoise.NoiseParameters> temperature,
			ResourceKey<NormalNoise.NoiseParameters> vegetation,
			ResourceKey<NormalNoise.NoiseParameters> continentalness,
			ResourceKey<NormalNoise.NoiseParameters> erosion
		) {
		register(bootstapContext, temperature, -10 + i, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
		register(bootstapContext, vegetation, -8 + i, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
		register(bootstapContext, continentalness, -9 + i, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0);
		register(bootstapContext, erosion, -10 + i, 1.0, 1.0, 0.0, 1.0, 0.0, 2.0, 0.0);
	}

	public static void register(BootstapContext<NormalNoise.NoiseParameters> bootstapContext, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double ... amplitudes) {
		bootstapContext.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
	}
}
