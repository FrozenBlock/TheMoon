package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.util.gravity.impl.LevelGravityInterface;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin implements LevelGravityInterface {

	@Unique
	private double theMoon$gravity;

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void theMoon$setGravity(CallbackInfo info) {
		Level level = Level.class.cast(this);
		this.theMoon$gravity = level.dimensionTypeId() != TheMoonDimensionTypes.MOON ? 1.0 : 0.1;
	}

	@Unique
	@Override
	public double getGravity() {
		return this.theMoon$gravity;
	}
}
