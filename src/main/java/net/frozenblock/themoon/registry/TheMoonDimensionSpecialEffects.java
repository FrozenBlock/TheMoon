package net.frozenblock.themoon.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TheMoonDimensionSpecialEffects {
	public static final ResourceLocation MOON_EFFECTS = TheMoonSharedConstants.id("the_moon");

	@Environment(EnvType.CLIENT)
	public static class MoonEffects extends DimensionSpecialEffects {
		public MoonEffects() {
			super(Float.NaN, false, SkyType.NORMAL, true, false);
		}

		@Override
		public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
			return fogColor.scale(0.15f);
		}

		@Override
		public boolean isFoggyAt(int x, int y) {
			return false;
		}

		@Override
		@Nullable
		public float[] getSunriseColor(float timeOfDay, float partialTicks) {
			return null;
		}
	}

}
