package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.minecraft.core.BlockPos;
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

	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;canSpawnSprintParticle()Z", shift = At.Shift.BEFORE))
	public void theMoon$baseTick(CallbackInfo info) {
		this.theMoon$spawnMoonDustParticle();
	}

	@Unique
	private void theMoon$spawnMoonDustParticle() {
		Entity entity = Entity.class.cast(this);
		double length;
		if (entity.level.getRandom().nextBoolean() && !entity.isPassenger() && (!(entity instanceof LivingEntity livingEntity) || !livingEntity.isFallFlying()) && entity.isOnGround() && ((length = entity.getDeltaMovement().length()) > 0.075 || length > 0.025 && entity.level.getRandom().nextBoolean())) {
			int i = Mth.floor(entity.getX());
			BlockPos blockPos = new BlockPos(i, Mth.floor(entity.getY() - (double) 0.2f), Mth.floor(entity.getZ()));
			BlockState blockState = entity.level.getBlockState(blockPos);
			if (blockState.is(TheMoonBlockTags.MOON_DUST)) {
				Vec3 vec3 = entity.getDeltaMovement();
				entity.level.addParticle(TheMoonParticleTypes.MOON_DUST, entity.getX() + (this.random.nextDouble() - 0.5) * (double) this.dimensions.width, entity.getY() + 0.1, entity.getZ() + (this.random.nextDouble() - 0.5) * (double) this.dimensions.width, vec3.x * -0.5, 0.065, vec3.z * -0.5);
			}
		}
	}

}
