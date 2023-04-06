package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonDimensionTypes;
import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;handleNetherPortal()V", shift = At.Shift.AFTER))
	public void theMoon$switchDimensions(CallbackInfo info) {
		Entity entity = Entity.class.cast(this);
		Level level = entity.level;
		if (level instanceof ServerLevel serverLevel) {
			if (!entity.isRemoved()) {
				if (entity.canChangeDimensions() && !entity.isPassenger()) {

					if (level.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD) {
						if (entity.getBlockY() > 384) {
							ServerLevel exosphere = serverLevel.getServer().getLevel(TheMoonDimensionTypes.EXOSPHERE_LEVEL);
							if (exosphere != null) {
								entity.setDeltaMovement(entity.getDeltaMovement().add(0, 3, 0));
								entity.teleportTo(exosphere, entity.getX(), 64, entity.getZ(), Set.of(), entity.getYRot(), 90.0f);
							}
						}
					}

					if (level.dimensionTypeId() == TheMoonDimensionTypes.EXOSPHERE) {
						if (entity.getBlockY() > 200) {
							ServerLevel moon = serverLevel.getServer().getLevel(TheMoonDimensionTypes.MOON_LEVEL);
							if (moon != null) {
								entity.setDeltaMovement(entity.getDeltaMovement().add(0, -1, 0));
								entity.teleportTo(moon, entity.getX(), 336, entity.getZ(), Set.of(), entity.getYRot(), 90.0f);
							}
						} else if (entity.getBlockY() < -32) {
							ServerLevel overworld = serverLevel.getServer().overworld();
							if (overworld != null) {
								entity.teleportTo(overworld, entity.getX(), 380, entity.getZ(), Set.of(), entity.getYRot(), 90.0f);
							}
						}
					}

					if (level.dimensionTypeId() == TheMoonDimensionTypes.MOON) {
						if (entity.getBlockY() > 334) {
							ServerLevel exosphere = serverLevel.getServer().getLevel(TheMoonDimensionTypes.EXOSPHERE_LEVEL);
							if (exosphere != null) {
								entity.teleportTo(exosphere, entity.getX(), 195, entity.getZ(), Set.of(), entity.getYRot(), 90.0f);
							}
						}
					}

				}
			}
		}
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
					if (length > 0 && random.nextFloat() * 6.5 < length) {
						entity.level.addParticle(TheMoonParticleTypes.MOON_DUST, entity.getX() + (this.random.nextDouble() - 0.5) * (double) this.dimensions.width, entity.getY() + 0.1, entity.getZ() + (this.random.nextDouble() - 0.5) * (double) this.dimensions.width, vec3.x * -0.5, 0.0175, vec3.z * -0.5);
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
