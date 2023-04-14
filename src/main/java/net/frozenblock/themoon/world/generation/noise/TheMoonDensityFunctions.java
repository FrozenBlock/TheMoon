package net.frozenblock.themoon.world.generation.noise;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class TheMoonDensityFunctions {
	private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
	private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
	public static final ResourceKey<DensityFunction> BASE_3D_NOISE_MOON = createKey("the_moon/base_3d_noise");
	public static final ResourceKey<DensityFunction> CONTINENTS = createKey("the_moon/continents");
	public static final ResourceKey<DensityFunction> EROSION = createKey("the_moon/erosion");
	public static final ResourceKey<DensityFunction> CRATERS = createKey("the_moon/craters");
	public static final ResourceKey<DensityFunction> OFFSET = createKey("the_moon/offset");
	public static final ResourceKey<DensityFunction> FACTOR = createKey("the_moon/factor");
	public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("the_moon/jaggedness");
	public static final ResourceKey<DensityFunction> DEPTH = createKey("the_moon/depth");
	public static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("the_moon/sloped_cheese");
	public static void bootstrap(BootstapContext<DensityFunction> context) {
		HolderGetter<NormalNoise.NoiseParameters> holderGetter = context.lookup(Registries.NOISE);
		HolderGetter<DensityFunction> holderGetter2 = context.lookup(Registries.DENSITY_FUNCTION);
		DensityFunction shiftX = registerAndWrap(context, SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(holderGetter.getOrThrow(Noises.SHIFT)))));
		DensityFunction shiftZ = registerAndWrap(context, SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(holderGetter.getOrThrow(Noises.SHIFT)))));

		Holder<DensityFunction> continents = context.register(CONTINENTS, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, holderGetter.getOrThrow(TheMoonNoises.CONTINENTALNESS))));
		Holder<DensityFunction> erosion = context.register(EROSION, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.35, holderGetter.getOrThrow(TheMoonNoises.EROSION))));
		DensityFunction jagged = DensityFunctions.noise(holderGetter.getOrThrow(Noises.JAGGED), 1000.0, 0.0);

		registerTerrainNoises(context, holderGetter2, jagged, continents, erosion, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);

		context.register(BASE_3D_NOISE_MOON, BlendedNoise.createUnseeded(0.25, 0.45, 80.0, 160.0, 6.0));
		context.register(CRATERS, craters(holderGetter));
	}

	private static void registerTerrainNoises(BootstapContext<DensityFunction> context, HolderGetter<DensityFunction> densityFunctionGetter, DensityFunction jaggedNoise, Holder<DensityFunction> continents, Holder<DensityFunction> erosion, ResourceKey<DensityFunction> offsetKey, ResourceKey<DensityFunction> factorKey, ResourceKey<DensityFunction> jaggednessKey, ResourceKey<DensityFunction> depthKey, ResourceKey<DensityFunction> slopedCheeseKey, boolean amplified) {
		DensityFunctions.Spline.Coordinate coordinate = new DensityFunctions.Spline.Coordinate(continents);
		DensityFunctions.Spline.Coordinate coordinate2 = new DensityFunctions.Spline.Coordinate(erosion);
		DensityFunctions.Spline.Coordinate coordinate3 = new DensityFunctions.Spline.Coordinate(densityFunctionGetter.getOrThrow(NoiseRouterData.RIDGES));
		DensityFunctions.Spline.Coordinate coordinate4 = new DensityFunctions.Spline.Coordinate(densityFunctionGetter.getOrThrow(NoiseRouterData.RIDGES_FOLDED));
		DensityFunction densityFunction = registerAndWrap(context, offsetKey, splineWithBlending(DensityFunctions.add(DensityFunctions.constant(-0.5037500262260437), DensityFunctions.spline(TheMoonTerrainProvider.moonOffset(coordinate, coordinate2, coordinate4, amplified))), DensityFunctions.blendOffset()));
		DensityFunction densityFunction2 = registerAndWrap(context, factorKey, splineWithBlending(DensityFunctions.spline(TheMoonTerrainProvider.moonFactor(coordinate, coordinate2, coordinate3, coordinate4, amplified)), DensityFunctions.constant(7.0)));
		DensityFunction densityFunction3 = registerAndWrap(context, depthKey, DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), densityFunction));
		DensityFunction densityFunction4 = registerAndWrap(context, jaggednessKey, splineWithBlending(DensityFunctions.spline(TerrainProvider.overworldJaggedness(coordinate, coordinate2, coordinate3, coordinate4, amplified)), DensityFunctions.zero()));
		DensityFunction densityFunction5 = DensityFunctions.mul(densityFunction4, jaggedNoise.abs());
		DensityFunction densityFunction6 = noiseGradientDensity(densityFunction2, DensityFunctions.add(densityFunction3, densityFunction5));
		context.register(slopedCheeseKey, DensityFunctions.add(densityFunction6, getFunction(densityFunctionGetter, BASE_3D_NOISE_MOON)));
	}

	private static DensityFunction craters(HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
		DensityFunction densityFunction = DensityFunctions.noise(noiseParameters.getOrThrow(TheMoonNoises.CRATER), 12.0, 2);
		DensityFunction densityFunction2 = DensityFunctions.mappedNoise(noiseParameters.getOrThrow(TheMoonNoises.CRATER_RARENESS), 0.0, -2.0);
		DensityFunction densityFunction3 = DensityFunctions.mappedNoise(noiseParameters.getOrThrow(TheMoonNoises.CRATER_THICKNESS), 2.0, 1.1);
		DensityFunction densityFunction4 = DensityFunctions.add(DensityFunctions.mul(densityFunction, DensityFunctions.constant(2.0)), densityFunction2);
		return DensityFunctions.cacheOnce(DensityFunctions.mul(densityFunction4, densityFunction3.square()));
	}

	private static DensityFunction splineWithBlending(DensityFunction densityFunction, DensityFunction densityFunction2) {
		DensityFunction densityFunction3 = DensityFunctions.lerp(DensityFunctions.blendAlpha(), densityFunction2, densityFunction);
		return DensityFunctions.flatCache(DensityFunctions.cache2d(densityFunction3));
	}

	private static DensityFunction noiseGradientDensity(DensityFunction densityFunction, DensityFunction densityFunction2) {
		DensityFunction densityFunction3 = DensityFunctions.mul(densityFunction2, densityFunction);
		return DensityFunctions.mul(DensityFunctions.constant(4.0), densityFunction3.quarterNegative());
	}

	private static DensityFunction registerAndWrap(BootstapContext<DensityFunction> context, ResourceKey<DensityFunction> key, DensityFunction densityFunction) {
		return new DensityFunctions.HolderHolder(context.register(key, densityFunction));
	}

	private static DensityFunction getFunction(HolderGetter<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
		return new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(key));
	}

	private static ResourceKey<DensityFunction> createKey(String location) {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, TheMoonSharedConstants.id(location));
	}

	private static ResourceKey<DensityFunction> createVanillaKey(String location) {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(location));
	}
}
