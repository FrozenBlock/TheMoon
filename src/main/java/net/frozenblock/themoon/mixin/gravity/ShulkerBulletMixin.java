package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {

	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
	public void theMoon$useGravity(Args args) {
		ShulkerBullet shulkerBullet = ShulkerBullet.class.cast(this);
		args.set(1, -(double)((EntityGravityInterface)shulkerBullet).getEffectiveGravity());
	}

}
