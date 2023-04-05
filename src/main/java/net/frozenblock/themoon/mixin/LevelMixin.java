package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

	@Inject(method = "getSeaLevel", at = @At("HEAD"), cancellable = true)
	public void theMoon$getSeaLevel(CallbackInfoReturnable<Integer> info) {
		if (Level.class.cast(this).dimensionTypeId() == TheMoonDimensionTypes.MOON) {
			info.setReturnValue(-64);
		}
	}
}
