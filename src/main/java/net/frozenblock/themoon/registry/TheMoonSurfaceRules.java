package net.frozenblock.themoon.registry;

import net.frozenblock.lib.worldgen.surface.FrozenSurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class TheMoonSurfaceRules {

	public static SurfaceRules.RuleSource moon() {
		return SurfaceRules.sequence(
				SurfaceRules.ifTrue(
						SurfaceRules.verticalGradient(
								"bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)
						),
						FrozenSurfaceRules.BEDROCK
				),
				FrozenSurfaceRules.makeStateRule(TheMoonBlocks.MOON_ROCK)
		);
	}

}
