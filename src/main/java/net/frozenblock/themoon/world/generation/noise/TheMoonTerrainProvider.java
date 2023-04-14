package net.frozenblock.themoon.world.generation.noise;

import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.level.levelgen.NoiseRouterData;

public class TheMoonTerrainProvider {
	private static final ToFloatFunction<Float> NO_TRANSFORM = ToFloatFunction.IDENTITY;
	private static final ToFloatFunction<Float> AMPLIFIED_OFFSET = ToFloatFunction.createUnlimited(f -> f < 0.0f ? f : f * 2.0f);
	private static final ToFloatFunction<Float> AMPLIFIED_FACTOR = ToFloatFunction.createUnlimited(f -> 1.25f - 6.25f / (f + 5.0f));
	private static final ToFloatFunction<Float> AMPLIFIED_JAGGEDNESS = ToFloatFunction.createUnlimited(f -> f * 2.0f);

	public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> moonOffset(I toFloatFunction, I toFloatFunction2, I toFloatFunction3, boolean bl) {
		ToFloatFunction<Float> toFloatFunction4 = bl ? AMPLIFIED_OFFSET : NO_TRANSFORM;
		CubicSpline<C, I> cubicSpline = TerrainProvider.buildErosionOffsetSpline(toFloatFunction2, toFloatFunction3, -0.15f, 0.0f, 0.0f, 0.1f, 0.0f, -0.03f, false, false, toFloatFunction4);
		CubicSpline<C, I> cubicSpline2 = TerrainProvider.buildErosionOffsetSpline(toFloatFunction2, toFloatFunction3, -0.1f, 0.03f, 0.1f, 0.1f, 0.01f, -0.03f, false, false, toFloatFunction4);
		CubicSpline<C, I> cubicSpline3 = TerrainProvider.buildErosionOffsetSpline(toFloatFunction2, toFloatFunction3, -0.1f, 0.03f, 0.1f, 0.7f, 0.01f, -0.03f, true, true, toFloatFunction4);
		CubicSpline<C, I> cubicSpline4 = TerrainProvider.buildErosionOffsetSpline(toFloatFunction2, toFloatFunction3, -0.05f, 0.03f, 0.1f, 1.0f, 0.01f, 0.01f, true, true, toFloatFunction4);
		return CubicSpline.builder(toFloatFunction, toFloatFunction4).addPoint(-1.1f, 0.044f).addPoint(-1.02f, -0.2222f).addPoint(-0.51f, -0.2222f).addPoint(-0.44f, -0.12f).addPoint(-0.18f, -0.12f).addPoint(-0.16f, cubicSpline).addPoint(-0.15f, cubicSpline).addPoint(-0.1f, cubicSpline2).addPoint(0.25f, cubicSpline3).addPoint(1.0f, cubicSpline4).build();
	}

	public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> moonFactor(I toFloatFunction, I toFloatFunction2, I toFloatFunction3, I toFloatFunction4, boolean bl) {
		ToFloatFunction<Float> toFloatFunction5 = bl ? AMPLIFIED_FACTOR : NO_TRANSFORM;
		return CubicSpline.builder(toFloatFunction, NO_TRANSFORM).addPoint(-0.19f, 3.95f).addPoint(-0.15f, getErosionFactor(toFloatFunction2, toFloatFunction3, toFloatFunction4, 6.25f, true, NO_TRANSFORM)).addPoint(-0.1f, getErosionFactor(toFloatFunction2, toFloatFunction3, toFloatFunction4, 5.47f, true, toFloatFunction5)).addPoint(0.01f, getErosionFactor(toFloatFunction2, toFloatFunction3, toFloatFunction4, 5.08f, true, toFloatFunction5)).addPoint(0.15f, getErosionFactor(toFloatFunction2, toFloatFunction3, toFloatFunction4, 4.69f, false, toFloatFunction5)).build();
	}

	public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> moonJaggedness(I toFloatFunction, I toFloatFunction2, I toFloatFunction3, I toFloatFunction4, boolean bl) {
		ToFloatFunction<Float> toFloatFunction5 = bl ? AMPLIFIED_JAGGEDNESS : NO_TRANSFORM;
		return CubicSpline.builder(toFloatFunction, toFloatFunction5).addPoint(-0.11f, 0.0f).addPoint(0.03f, buildErosionJaggednessSpline(toFloatFunction2, toFloatFunction3, toFloatFunction4, 1.0f, 0.5f, 0.0f, 0.0f, toFloatFunction5)).addPoint(0.65f, buildErosionJaggednessSpline(toFloatFunction2, toFloatFunction3, toFloatFunction4, 1.0f, 1.0f, 1.0f, 0.0f, toFloatFunction5)).build();
	}

	private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionJaggednessSpline(I toFloatFunction, I toFloatFunction2, I toFloatFunction3, float f, float g, float h, float i, ToFloatFunction<Float> toFloatFunction4) {
		CubicSpline<C, I> cubicSpline = buildRidgeJaggednessSpline(toFloatFunction2, toFloatFunction3, f, h, toFloatFunction4);
		CubicSpline<C, I> cubicSpline2 = buildRidgeJaggednessSpline(toFloatFunction2, toFloatFunction3, g, i, toFloatFunction4);
		return CubicSpline.builder(toFloatFunction, toFloatFunction4).addPoint(-1.0f, cubicSpline).addPoint(-0.78f, cubicSpline2).addPoint(-0.5775f, cubicSpline2).addPoint(-0.375f, 0.0f).build();
	}

	private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildRidgeJaggednessSpline(I toFloatFunction, I toFloatFunction2, float f, float g, ToFloatFunction<Float> toFloatFunction3) {
		float h = NoiseRouterData.peaksAndValleys(0.4f);
		float i = NoiseRouterData.peaksAndValleys(0.56666666f);
		float j = (h + i) / 2.0f;
		CubicSpline.Builder<C, I> builder = CubicSpline.builder(toFloatFunction2, toFloatFunction3);
		builder.addPoint(h, 0.0f);
		if (g > 0.0f) {
			builder.addPoint(j, buildWeirdnessJaggednessSpline(toFloatFunction, g, toFloatFunction3));
		} else {
			builder.addPoint(j, 0.0f);
		}
		if (f > 0.0f) {
			builder.addPoint(1.0f, buildWeirdnessJaggednessSpline(toFloatFunction, f, toFloatFunction3));
		} else {
			builder.addPoint(1.0f, 0.0f);
		}
		return builder.build();
	}

	private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildWeirdnessJaggednessSpline(I toFloatFunction, float f, ToFloatFunction<Float> toFloatFunction2) {
		float g = 0.63f * f;
		float h = 0.3f * f;
		return CubicSpline.builder(toFloatFunction, toFloatFunction2).addPoint(-0.01f, g).addPoint(0.02f, h).build();
	}

	private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I toFloatFunction, I toFloatFunction2, I toFloatFunction3, float f, boolean bl, ToFloatFunction<Float> toFloatFunction4) {
		CubicSpline cubicSpline = CubicSpline.builder(toFloatFunction2, toFloatFunction4).addPoint(-0.2f, 6.3f).addPoint(0.2f, f).build();
		CubicSpline.Builder builder = CubicSpline.builder(toFloatFunction, toFloatFunction4).addPoint(-0.6f, cubicSpline).addPoint(-0.5f, CubicSpline.builder(toFloatFunction2, toFloatFunction4).addPoint(-0.05f, 6.3f).addPoint(0.05f, 2.67f).build()).addPoint(-0.35f, cubicSpline).addPoint(-0.25f, cubicSpline).addPoint(-0.1f, CubicSpline.builder(toFloatFunction2, toFloatFunction4).addPoint(-0.05f, 2.67f).addPoint(0.05f, 6.3f).build()).addPoint(0.03f, cubicSpline);
		if (bl) {
			CubicSpline cubicSpline2 = CubicSpline.builder(toFloatFunction2, toFloatFunction4).addPoint(0.0f, f).addPoint(0.1f, 0.625f).build();
			CubicSpline cubicSpline3 = CubicSpline.builder(toFloatFunction3, toFloatFunction4).addPoint(-0.9f, f).addPoint(-0.69f, cubicSpline2).build();
			builder.addPoint(0.35f, f).addPoint(0.45f, cubicSpline3).addPoint(0.55f, cubicSpline3).addPoint(0.62f, f);
		} else {
			CubicSpline cubicSpline2 = CubicSpline.builder(toFloatFunction3, toFloatFunction4).addPoint(-0.7f, cubicSpline).addPoint(-0.15f, 1.37f).build();
			CubicSpline cubicSpline3 = CubicSpline.builder(toFloatFunction3, toFloatFunction4).addPoint(0.45f, cubicSpline).addPoint(0.7f, 1.56f).build();
			builder.addPoint(0.05f, cubicSpline3).addPoint(0.4f, cubicSpline3).addPoint(0.45f, cubicSpline2).addPoint(0.55f, cubicSpline2).addPoint(0.58f, f);
		}
		return builder.build();
	}

}
