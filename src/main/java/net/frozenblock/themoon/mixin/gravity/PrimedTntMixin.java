package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PrimedTnt.class)
public class PrimedTntMixin {

	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
	public void theMoon$useGravity(Args args) {
		double y = (double)args.get(1) + (double)0.04F;
		PrimedTnt primedTnt = PrimedTnt.class.cast(this);
		args.set(1, y -(double)((EntityGravityInterface)primedTnt).getEffectiveGravity());
	}

}
