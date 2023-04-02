package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(DDD)V", ordinal = 0))
	public void theMoon$useGravity(Args args) {
		double y = (double)args.get(1) + (double)0.05F;
		AbstractArrow abstractArrow = AbstractArrow.class.cast(this);
		args.set(1, y -(double)((EntityGravityInterface)abstractArrow).getEffectiveGravity());
	}

}
