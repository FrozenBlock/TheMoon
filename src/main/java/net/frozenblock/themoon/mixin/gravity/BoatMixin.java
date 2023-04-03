package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Boat.class)
public class BoatMixin {

	@ModifyArgs(method = "floatBoat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;setDeltaMovement(DDD)V", ordinal = 0))
	public void theMoon$useGravity(Args args) {
		double y = (double)args.get(1) + (double)0.04F;
		Boat boat = Boat.class.cast(this);
		args.set(1, y -(double)((EntityGravityInterface)boat).getEffectiveGravity());
	}

}
