package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow
	private EntityDimensions dimensions;
	@Shadow @Final
	public RandomSource random;
	@Shadow
	private float yRot;

	@Inject(method = "baseTick", at = @At("HEAD"))
	public void theMoon$baseTick(CallbackInfo info) {
		this.theMoon$spawnMoonDustParticle();
	}

	@Unique
	private void theMoon$spawnMoonDustParticle() {
		Entity entity = Entity.class.cast(this);
		double length = entity.walkDist - entity.walkDistO;
		length *= 2;
		if (!entity.isPassenger() && (!(entity instanceof LivingEntity livingEntity) || !livingEntity.isFallFlying()) && entity.isOnGround()) {
			int i = Mth.floor(entity.getX());
			BlockPos blockPos = new BlockPos(i, Mth.floor(entity.getY() - (double) 0.2f), Mth.floor(entity.getZ()));
			BlockState blockState = entity.level.getBlockState(blockPos);
			if (blockState.is(TheMoonBlockTags.MOON_DUST)) {
				Vec3 vec3 = entity.getDeltaMovement();
				for (int c = 0; c < 3; c++) {
					if (length > 0 && random.nextFloat() * 8 < length) {
						entity.level.addParticle(TheMoonParticleTypes.MOON_DUST, entity.getX() + (this.random.nextDouble() - 0.5) * (double) this.dimensions.width, entity.getY() + 0.1, entity.getZ() + (this.random.nextDouble() - 0.5) * (double) this.dimensions.width, vec3.x * -0.5, 0.0325, vec3.z * -0.5);
					}
				}
			}
		}
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity$MovementEmission;emitsEvents()Z", ordinal = 1, shift = At.Shift.BEFORE))
	public void theMoon$youllStumbleInMyFootsteps(CallbackInfo info) {
		if (this.theMoon$keepTheSameAppointmentsIKept()) {
			this.theMoon$ifYouTryWalkingInMyShoes();
		}
	}

	@Unique
	public boolean theMoon$keepTheSameAppointmentsIKept() {
		Entity entity = Entity.class.cast(this);
		return entity instanceof LivingEntity livingEntity && !livingEntity.isInWater() && !livingEntity.isSpectator() && !livingEntity.isInLava() && livingEntity.isAlive()
				&& entity.level.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - 0.1, entity.getZ())).is(TheMoonBlockTags.MOON_DUST);
	}

	@Unique
	public void theMoon$ifYouTryWalkingInMyShoes() {
		Entity entity = Entity.class.cast(this);
		entity.level.addParticle(TheMoonParticleTypes.FOOTSTEP, entity.getX(), entity.getY() + 0.001, entity.getZ(), this.yRot, 0.0, 0.0);
	}
}
