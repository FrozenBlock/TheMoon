package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements EntityGravityInterface {

	@Shadow
	public Level level;

	@Shadow
	public float fallDistance;

	@Inject(method = "checkFallDamage", at = @At("TAIL"))
	private void theMoon$checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos, CallbackInfo info) {
		this.fallDistance *= GravityCalculator.calculateGravity(Entity.class.cast(this));
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
		return this.theMoon$getGravity() * ((float) GravityCalculator.calculateGravity(entity));
	}
}
