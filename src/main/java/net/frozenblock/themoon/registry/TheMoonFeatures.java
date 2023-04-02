package net.frozenblock.themoon.registry;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.world.generation.features.CraterFeature;
import net.frozenblock.themoon.world.generation.features.config.CraterFeatureConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class TheMoonFeatures {

	public static final CraterFeature CRATER = new CraterFeature(CraterFeatureConfiguration.CODEC);

	public static void register() {
		Registry.register(BuiltInRegistries.FEATURE, TheMoonSharedConstants.id("crater"), CRATER);
	}

}
