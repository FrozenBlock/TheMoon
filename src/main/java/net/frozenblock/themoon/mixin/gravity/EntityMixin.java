package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
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
		Entity entity = Entity.class.cast(this);
		float fallDistance = args.get(4);
		args.set(4, fallDistance * (float) GravityCalculator.calculateGravity(this.level, entity.position().y()));
	}

	@Unique
	@Override
	public float theMoon$getGravity() {
		return 0.04F;
	}

	@Unique
	@Override
	public float getEffectiveGravity() {
		Entity entity = Entity.class.cast(this);
		return this.theMoon$getGravity() * ((float) GravityCalculator.calculateGravity(entity.level, entity.position().y()));
	}
}
