package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.api.GravityGetter;
import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Entity.class)
public class EntityMixin implements EntityGravityInterface {

	@Shadow
	public Level level;

	@ModifyArgs(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V"))
	protected void checkFallDamage(Args args) {
		float fallDistance = args.get(4);
		args.set(4, fallDistance * (float)GravityGetter.getGravity(this.level));
	}

	@Unique
	@Override
	public float theMoon$getGravity() {
		return 0.04F;
	}

	@Unique
	@Override
	public float getEffectiveGravity() {
		return this.theMoon$getGravity() * ((float) GravityGetter.getGravity(Entity.class.cast(this).level));
	}
}
