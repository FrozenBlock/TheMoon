package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@ModifyVariable(method = "travel", at = @At(value = "STORE", ordinal = 0))
	public double theMoon$Gravity(double original) {
		double copiedOriginal = original;
		LivingEntity entity = LivingEntity.class.cast(this);
		if (entity.level.dimensionTypeId() == TheMoonDimensionTypes.MOON) {
			double e = entity.getY();
			if (e < 286.0) {
				double gravity = 1.0F - e / 286.0F;
				double g = Math.signum(gravity);
				double h = g * Math.max(Math.abs(gravity), 0.2);
				copiedOriginal = 0.02 * h;
			} else {
				double f = entity.getDeltaMovement().y;
				copiedOriginal = Math.signum(f == 0.0 ? 1.0 : -f) * 0.02;
			}
			return copiedOriginal;
		}
		return original;
	}
}
