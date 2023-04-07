package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Rabbit.class)
public class RabbitMixin {

	@Inject(method = "getJumpPower", at = @At("RETURN"), cancellable = true)
	public void theMoon$getJumpPower(CallbackInfoReturnable<Float> info) {
		if (LivingEntity.class.cast(this).level.dimensionTypeId() == TheMoonDimensionTypes.EXOSPHERE) {
			info.setReturnValue(info.getReturnValue() + 0.3F);
		}
	}

}
