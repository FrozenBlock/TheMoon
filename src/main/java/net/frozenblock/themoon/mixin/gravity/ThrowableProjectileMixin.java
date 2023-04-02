package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ThrowableProjectile.class)
public abstract class ThrowableProjectileMixin implements EntityGravityInterface {

	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;setDeltaMovement(DDD)V", ordinal = 0))
	public void theMoon$useGravity(Args args) {
		ThrowableProjectile throwableProjectile = ThrowableProjectile.class.cast(this);
		double y = (double)args.get(1) + (double)this.getGravity();
		args.set(1, y -(double)((EntityGravityInterface)throwableProjectile).theMoon$getGravity());
	}

	@Shadow
	public float getGravity() {
		throw new AssertionError("Mixin injection failed - The Moon ThrowableProjectileMixin");
	}

	@Override
	public float theMoon$getGravity() {
		return this.getGravity();
	}

}
