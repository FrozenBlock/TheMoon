package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

	@Inject(method = "getSeaLevel", at = @At("HEAD"), cancellable = true)
	public void theMoon$getSeaLevel(CallbackInfoReturnable<Integer> info) {
		ResourceKey<DimensionType> dimensionType = Level.class.cast(this).dimensionTypeId();
		if (dimensionType == TheMoonDimensionTypes.MOON || dimensionType == TheMoonDimensionTypes.EXOSPHERE) {
			info.setReturnValue(-64);
		}
	}

}
