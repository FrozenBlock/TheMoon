package net.frozenblock.themoon.mixin.gravity.client;

import net.frozenblock.themoon.util.gravity.api.GravityCalculator;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class ParticleMixin {

	@Unique
	private double theMoon$oldYD;

	@Shadow
	public double yd;

	@Shadow
	public float gravity;

	@Shadow @Final
	public ClientLevel level;

	@Shadow
	public double y;

	@Inject(method = "tick", at = @At("HEAD"))
	public void theMoon$storeY(CallbackInfo info) {
		this.theMoon$oldYD = this.yd;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;move(DDD)V", ordinal = 0, shift = Shift.BEFORE))
	public void theMoon$useGravity(CallbackInfo info) {
		this.yd = this.theMoon$oldYD - (0.04 * GravityCalculator.calculateGravity(this.level, this.y) * (double)this.gravity);
	}

}
