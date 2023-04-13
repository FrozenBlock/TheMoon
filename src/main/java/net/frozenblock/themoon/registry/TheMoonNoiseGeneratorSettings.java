package net.frozenblock.themoon.registry;

import java.util.List;
import java.util.stream.Stream;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.OreVeinifier;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TheMoonNoiseGeneratorSettings {

	public static final ResourceKey<NoiseGeneratorSettings> MOON = ResourceKey.create(Registries.NOISE_SETTINGS, TheMoonSharedConstants.id("the_moon"));
	public static final ResourceKey<NoiseGeneratorSettings> EXOSPHERE = ResourceKey.create(Registries.NOISE_SETTINGS, TheMoonSharedConstants.id("the_exosphere"));

	public static final NoiseSettings MOON_NOISE_SETTINGS = NoiseSettings.create(-64, 208, 2, 1);
	public static final NoiseSettings EXOSPHERE_NOISE_SETTINGS = NoiseSettings.create(0, 32, 2, 1);

	protected static NoiseRouter moonNoiseRouter(HolderGetter<DensityFunction> densityGetter, HolderGetter<NormalNoise.NoiseParameters> noiseGetter) {
		DensityFunction densityFunction = NoiseRouterData.postProcess(slideMoon(NoiseRouterData.getFunction(densityGetter, NoiseRouterData.DEPTH)));
		int i = Stream.of(OreVeinifier.VeinType.values()).mapToInt(veinType -> 2).min().orElse(-DimensionType.MIN_Y * 2);
		int j = Stream.of(OreVeinifier.VeinType.values()).mapToInt(veinType -> 40).max().orElse(-DimensionType.MIN_Y * 2);
		DensityFunction Y = NoiseRouterData.getFunction(densityGetter, ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation("y")));

		DensityFunction densityFunction12 = NoiseRouterData.getFunction(densityGetter, createKey("overworld/sloped_cheese"));
		DensityFunction densityFunction13 = DensityFunctions.min(densityFunction12, DensityFunctions.mul(DensityFunctions.constant(5.0), NoiseRouterData.getFunction(densityGetter, ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation("overworld/caves/entrances")))));
		DensityFunction densityFunction14 = DensityFunctions.rangeChoice(densityFunction12, -1000000.0, 1.5625, densityFunction13, moonUnderground(densityGetter, noiseGetter, densityFunction12));
		DensityFunction densityFunction15 = DensityFunctions.min(NoiseRouterData.postProcess(slideMoon(densityFunction14)), NoiseRouterData.getFunction(densityGetter, createKey("overworld/caves/noodle")));

		return new NoiseRouter(
			DensityFunctions.zero(), //BARRIER NOISE
			DensityFunctions.zero(), //FLUID LEVEL FLOODEDNESS NOISE
			DensityFunctions.zero(), //FLUID LEVEL SPREAD NOISE
			DensityFunctions.zero(), //LAVA NOISE
			DensityFunctions.zero(), //TEMPERATURE
			DensityFunctions.zero(), //VEGETATION
			DensityFunctions.zero(), //CONTINENTS
			moonErosion(densityGetter, noiseGetter), //EROSION
			moonContinents(densityGetter, noiseGetter), //DEPTH
			NoiseRouterData.getFunction(densityGetter, NoiseRouterData.RIDGES), //RIDGES
			slideMoon(DensityFunctions.constant(-0.703125)), //INITIAL DENSITY WITHOUT JADDEGNESS
			densityFunction15, //FINAL DENSITY
			yLimitedInterpolatable(Y, DensityFunctions.noise(noiseGetter.getOrThrow(Noises.ORE_VEININESS), 1.5, 1.5), i, j, 0), //VEIN TOGGLE
			DensityFunctions.add(
				DensityFunctions.constant(-0.08f),
				DensityFunctions.max(
					yLimitedInterpolatable(Y, DensityFunctions.noise(noiseGetter.getOrThrow(Noises.ORE_VEIN_A), 4.0, 4.0), i, j, 0).abs(),
					yLimitedInterpolatable(Y, DensityFunctions.noise(noiseGetter.getOrThrow(Noises.ORE_VEIN_B), 4.0, 4.0), i, j, 0).abs()
				)
			), //VEIN RIDGED
			DensityFunctions.noise(noiseGetter.getOrThrow(Noises.ORE_GAP)) //VEIN GAP
		);
	}

	private static DensityFunction slideMoon(DensityFunction densityFunction) {
		return NoiseRouterData.slide(densityFunction, -64, 208, 80, 64, -0.1, 0, 24, 0.1);
	}

	public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {
		context.register(MOON, moon(context));
		context.register(EXOSPHERE, exosphere(context));
	}

	private static NoiseGeneratorSettings moon(BootstapContext<?> bootstapContext) {
		return new NoiseGeneratorSettings(MOON_NOISE_SETTINGS, TheMoonBlocks.MOON_ROCK.defaultBlockState(), Blocks.AIR.defaultBlockState(), moonNoiseRouter(bootstapContext.lookup(Registries.DENSITY_FUNCTION), bootstapContext.lookup(Registries.NOISE)), TheMoonSurfaceRules.moon(), List.of(), -64, false, false, false, false);
	}

	private static NoiseGeneratorSettings exosphere(BootstapContext<?> bootstapContext) {
		return new NoiseGeneratorSettings(EXOSPHERE_NOISE_SETTINGS, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), moonNoiseRouter(bootstapContext.lookup(Registries.DENSITY_FUNCTION), bootstapContext.lookup(Registries.NOISE)), TheMoonSurfaceRules.moon(), List.of(), 0, false, false, false, false);
	}

	private static DensityFunction yLimitedInterpolatable(DensityFunction input, DensityFunction whenInRange, int minInclusive, int maxInclusive, int maxRange) {
		return DensityFunctions.interpolated(DensityFunctions.rangeChoice(input, minInclusive, maxInclusive + 1, whenInRange, DensityFunctions.constant(maxRange)));
	}

	public static DensityFunction moonContinents(HolderGetter<DensityFunction> densityGetter, HolderGetter<NormalNoise.NoiseParameters> noiseGetter) {
		DensityFunction continents = DensityFunctions.zero();
		DensityFunction pillars = NoiseRouterData.getFunction(densityGetter, createKey("overworld/caves/pillars"));
		DensityFunction largePillars = DensityFunctions.add(pillars, pillars);
		DensityFunction comedicallyLargePillars = DensityFunctions.add(largePillars, largePillars);
		DensityFunction withErosion = DensityFunctions.rangeChoice(comedicallyLargePillars, -1000000.0, 0, continents, DensityFunctions.add(comedicallyLargePillars, continents));
		return withErosion;
	}

	public static DensityFunction moonErosion(HolderGetter<DensityFunction> densityGetter, HolderGetter<NormalNoise.NoiseParameters> noiseGetter) {
		DensityFunction erosionLarge = NoiseRouterData.getFunction(densityGetter, NoiseRouterData.EROSION_LARGE);
		DensityFunction pillars = NoiseRouterData.getFunction(densityGetter, createKey("overworld/caves/pillars"));
		DensityFunction largePillars = DensityFunctions.add(pillars, pillars);
		DensityFunction comedicallyLargePillars = DensityFunctions.add(largePillars, largePillars);
		DensityFunction withErosion = DensityFunctions.rangeChoice(comedicallyLargePillars, -1000000.0, 0, erosionLarge, DensityFunctions.add(comedicallyLargePillars, erosionLarge));
		return withErosion;
	}

	public static DensityFunction moonUnderground(HolderGetter<DensityFunction> holderGetter, HolderGetter<NormalNoise.NoiseParameters> holderGetter2, DensityFunction function) {
		DensityFunction spaghetti = NoiseRouterData.getFunction(holderGetter, createKey("overworld/caves/spaghetti_2d"));
		DensityFunction spaghettiRoughness = NoiseRouterData.getFunction(holderGetter, createKey("overworld/caves/spaghetti_roughness_function"));
		DensityFunction caveLayer = DensityFunctions.noise(holderGetter2.getOrThrow(Noises.CAVE_LAYER), 8.0); //8.0);
		DensityFunction tweakedCaveLayer = DensityFunctions.mul(DensityFunctions.constant(4.0), caveLayer.square()); //.square());
		DensityFunction cheeseCaves = DensityFunctions.noise(holderGetter2.getOrThrow(Noises.CAVE_CHEESE), 0.7666666666666666); //0.6666666666666666);
		DensityFunction tweakedCheeseCaves = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27), cheeseCaves).clamp(-1.0, 1.0), DensityFunctions.add(DensityFunctions.constant(1.5), DensityFunctions.mul(DensityFunctions.constant(-0.64), function)).clamp(0.0, 0.5));
		DensityFunction caveLayerAndCheese = DensityFunctions.add(tweakedCaveLayer, tweakedCheeseCaves);
		DensityFunction allCaves = DensityFunctions.min(DensityFunctions.min(caveLayerAndCheese, NoiseRouterData.getFunction(holderGetter, createKey("overworld/caves/entrances"))), DensityFunctions.add(spaghetti, spaghettiRoughness));
		return allCaves;
		//DensityFunction pillars = NoiseRouterData.getFunction(holderGetter, createKey("overworld/caves/pillars"));
		//DensityFunction pillarFunction = DensityFunctions.rangeChoice(pillars, -1000000.0, 0.03, DensityFunctions.constant(-1000000.0), pillars);
		//return DensityFunctions.max(allCaves, pillarFunction);
	}

	private static ResourceKey<DensityFunction> createKey(String id) {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(id));
	}
}
