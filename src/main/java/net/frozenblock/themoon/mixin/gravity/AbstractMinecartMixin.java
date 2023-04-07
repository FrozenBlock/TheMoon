package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {

	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
	public void theMoon$useGravity(Args args) {
		AbstractMinecart abstractMinecart = AbstractMinecart.class.cast(this);
		double gravity = abstractMinecart.isInWater() ? -0.005D : -0.04D;
		double y = (double)args.get(1) - gravity;
		boolean isDown = GravityCalculator.isGravityDown(abstractMinecart);
		args.set(1, y - (abstractMinecart.isInWater() && isDown ? 0.005 : (double)((EntityGravityInterface)abstractMinecart).getEffectiveGravity()));
	}
}
