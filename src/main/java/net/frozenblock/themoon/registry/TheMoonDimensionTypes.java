package net.frozenblock.themoon.registry;

import java.util.OptionalLong;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.DimensionType;

public class TheMoonDimensionTypes {
	public static final ResourceKey<DimensionType> MOON = register("the_moon");

	public static void bootstrap(BootstapContext<DimensionType> bootstapContext) {
		TheMoonSharedConstants.logMod("Registering Dimension Types for", TheMoonSharedConstants.UNSTABLE_LOGGING);

		bootstapContext.register(
				TheMoonDimensionTypes.MOON,
				new DimensionType(
						OptionalLong.empty(),
						false,
						false,
						false,
						false,
						1.0,
						false,
						false,
						0, 320, 320,
						BlockTags.INFINIBURN_END,
						TheMoonDimensionSpecialEffects.MOON_EFFECTS,
						0.0f,
						new DimensionType.MonsterSettings(
								false,
								false,
								UniformInt.of(0, 7),
								0)
				)
		);
	}

	private static ResourceKey<DimensionType> register(String name) {
		return ResourceKey.create(Registries.DIMENSION_TYPE, TheMoonSharedConstants.id(name));
	}
}
