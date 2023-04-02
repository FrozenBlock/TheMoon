package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@ModifyVariable(method = "travel", at = @At(value = "STORE", ordinal = 0))
	public double theMoon$Gravity(double original) {
		LivingEntity entity = LivingEntity.class.cast(this);
		return original * GravityCalculator.calculateGravity(entity.level, entity.position().y());
	}
}
