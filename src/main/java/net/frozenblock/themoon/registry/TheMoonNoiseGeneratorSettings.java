package net.frozenblock.themoon.registry;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import java.util.List;

public class TheMoonNoiseGeneratorSettings {

	public static final ResourceKey<NoiseGeneratorSettings> MOON = ResourceKey.create(Registries.NOISE_SETTINGS, TheMoonSharedConstants.id("moon"));

	public static final NoiseSettings MOON_NOISE_SETTINGS = NoiseSettings.create(0, 320, 2, 1);

	protected static NoiseRouter moonNoiseRouter(HolderGetter<DensityFunction> holderGetter) {
		DensityFunction densityFunction = NoiseRouterData.postProcess(slideMoon(NoiseRouterData.getFunction(holderGetter, NoiseRouterData.DEPTH)));
		return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), slideMoon(DensityFunctions.constant(-0.703125)), densityFunction, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
	}

	private static DensityFunction slideMoon(DensityFunction densityFunction) {
		return NoiseRouterData.slide(densityFunction, 0, 128, 80, 64, -0.1, 0, 24, 0.1);
	}

	private static NoiseGeneratorSettings moon(BootstapContext<?> bootstapContext) {
		return new NoiseGeneratorSettings(MOON_NOISE_SETTINGS, Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), moonNoiseRouter(bootstapContext.lookup(Registries.DENSITY_FUNCTION)), TheMoonSurfaceRules.moon(), List.of(), 0, false, false, true, false);
	}
}
