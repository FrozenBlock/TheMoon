package net.frozenblock.themoon.entity;

import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.themoon.entity.data.TheMoonEntityDataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Asteroid extends Mob {
	public float prevPitch;
	public float prevRoll;
	public float pitch;
	public float roll;
	public int ticksSinceActive;

	private static final EntityDataAccessor<State> STATE = SynchedEntityData.defineId(Asteroid.class, TheMoonEntityDataSerializers.ASTEROID_STATE);
	private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(Asteroid.class, EntityDataSerializers.FLOAT);

    public Asteroid(EntityType<Asteroid> entityType, Level level) {
		super(entityType, level);
		this.blocksBuilding = true;
	}

	@Override
	@NotNull
	public AABB makeBoundingBox() {
		return super.makeBoundingBox().inflate(this.getScale() / 2F);
	}

	public static AttributeSupplier.Builder addAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6D);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		Player closestPlayer = this.level.getNearestPlayer(this, -1.0);
		return closestPlayer == null || this.horizontalDistanceTo(closestPlayer.getX(), closestPlayer.getY()) > 128;
	}

	public double horizontalDistanceTo(double x, double z) {
		double d = this.getX() - x;
		double f = this.getZ() - z;
		return Math.sqrt(d * d + f * f);
	}

	@Override
	protected void doPush(@NotNull Entity entity) {
		if (this.getState() == State.FALLING || this.getBoundingBox().getSize() > entity.getBoundingBox().getSize() * 1.4) {
			super.doPush(entity);
		}
	}

	@Override
	protected void dropAllDeathLoot(@NotNull DamageSource damageSource) {
		if (!this.isSilkTouchOrShears(damageSource)) {
			super.dropAllDeathLoot(damageSource);
		}
	}

	private static final double windClamp = 0.3;

	@Override
	public void tick() {
		super.tick();
		if (this.wasTouchingWater && this.getState() != State.IDLE) {
			this.setState(State.IDLE);
			this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.5F, 0.8F);
			this.extinguishFire();
		}
		float rotationAmount = 10F;
		Vec3 deltaPosTest = this.getDeltaPos();
		Vec3 deltaPos = new Vec3(
				Math.abs(deltaPosTest.x()),
				Math.abs(deltaPosTest.y()),
				Math.abs(deltaPosTest.z())
		);
		if (deltaPos.horizontalDistance() != 0) {
			this.setYRot(-((float) (Mth.atan2(deltaPos.x * 10, deltaPos.z * 10) * 57.2957763671875D) - 90));
		}
		this.prevPitch = this.pitch;
		this.prevRoll = this.roll;
		float yRotAmount = (float) ((deltaPos.y * 0.5F) * rotationAmount);
		this.pitch += deltaPos.z * rotationAmount;
		this.roll += deltaPos.x * rotationAmount;
		this.pitch += yRotAmount;
		this.roll += yRotAmount;
		if (this.pitch > 360F) {
			this.pitch -= 360F;
			this.prevPitch -= 360F;
		}
		if (this.roll > 360F) {
			this.roll -= 360F;
			this.prevRoll -= 360F;
		}
		if (this.getState() == State.FALLING) {
			this.spawnFlameParticles();
			this.spawnSmokeParticles();
			this.setRemainingFireTicks(10);
		} else if (this.getState() == State.NO_GRAV) {
			if (this.level instanceof ServerLevel serverLevel) {
				Vec3 deltaMovement = this.getDeltaMovement();
				WindManager windManager = WindManager.getWindManager(serverLevel);
				Vec3 wind = windManager.getWindMovement3D(this.position(), 5, windClamp, 0.005);
				double windX = wind.x();
				double windY = wind.y() * 0.2;
				double windZ = wind.z();
				deltaMovement = deltaMovement.add((windX * 0.015), (windY * 0.015), (windZ * 0.015));
				this.setDeltaMovement(deltaMovement);
				this.spawnSmokeParticles();
			}
		} else {
			Player closestPlayer = this.level.getNearestPlayer(this, -1.0);
			if (!this.requiresCustomPersistence() && (((closestPlayer == null || closestPlayer.distanceTo(this) > 48)) || (this.wasTouchingWater))) {
				++this.ticksSinceActive;
				if (this.ticksSinceActive >= 400) {
					this.destroy(false);
				}
			} else {
				this.ticksSinceActive = 0;
			}
		}
	}

	public void destroy(boolean killed) {
		if (this.isAlive()) {
			this.playSound(SoundEvents.ANVIL_PLACE, this.getSoundVolume(), this.getVoicePitch());
		}
		this.spawnBreakParticles();
		this.remove(RemovalReason.KILLED);
	}

	public boolean isMovingTowards(@NotNull Entity entity) {
		return entity.getPosition(0).distanceTo(this.getPosition(0)) > entity.getPosition(1).distanceTo(this.getPosition(1));
	}

	public Vec3 getDeltaPos() {
		return this.getPosition(0).subtract(this.getPosition(1));
	}

	public boolean isSilkTouchOrShears(DamageSource damageSource) {
		if (damageSource.getDirectEntity() instanceof Player player) {
			ItemStack stack = player.getMainHandItem();
			return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0 || stack.is(Items.SHEARS);
		}
		return false;
	}

	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource source) {
		return source.is(DamageTypeTags.WITCH_RESISTANT_TO) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.FREEZE) || source.is(DamageTypes.SWEET_BERRY_BUSH) || source.is(DamageTypes.WITHER) || super.isInvulnerableTo(source);
	}

	@Override
	public boolean canFreeze() {
		return false;
	}

	@Override
	public boolean canBeLeashed(@NotNull Player player) {
		return false;
	}

	@Override
	public boolean canBeAffected(@NotNull MobEffectInstance effectInstance) {
		return false;
	}

	@Override
	public boolean canBeSeenAsEnemy() {
		return false;
	}

	@Override
	public boolean dampensVibrations() {
		return true;
	}

	@Override
	protected boolean canRide(@NotNull Entity vehicle) {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.5F;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return SoundEvents.ANVIL_PLACE;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ANVIL_PLACE;
	}

	@Override
	protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
		this.playSound(SoundEvents.ANVIL_PLACE, 0.2F, 1F);
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
		this.level.explode(this, this.getX(), this.getEyeY(), this.getZ(), Math.min(fallDistance, 7), Level.ExplosionInteraction.MOB);
		this.destroy(false);
		return true;
	}

	@Override
	protected float getWaterSlowDown() {
		return this.getState() == State.FALLING ? 1.0F : 0.8F;
	}

	@Override
	protected boolean updateInWaterStateAndDoFluidPushing() {
		if (this.getState() != State.IDLE) {
			this.fluidHeight.clear();
			return false;
		}
		return super.updateInWaterStateAndDoFluidPushing();
	}

	@Override
	public void knockback(double strength, double x, double z) {
		double scale = this.getScale() * this.getScale();
		super.knockback(strength, x / scale * 2, z / scale * 2);
	}

	@Override
	public float getScale() {
		return this.entityData.get(SCALE);
	}

	public void setScale(float value) {
		this.entityData.set(SCALE, value);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6F * (6F * value));
		this.setHealth(this.getMaxHealth());
	}

	public State getState() {
		return this.entityData.get(STATE);
	}

	public Asteroid setState(State state) {
		this.entityData.set(STATE, state);
		this.setNoGravity(state == State.NO_GRAV);
		return this;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(
				this.getId(),
				this.getUUID(),
				this.getX(),
				this.getY(),
				this.getZ(),
				this.pitch,
				this.roll,
				this.getType(),
				0,
				this.getDeltaMovement(),
				this.yHeadRot
		);
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		this.roll = packet.getYRot();
		this.pitch = packet.getXRot();
		this.syncPacketPositionCodec(d, e, f);
		this.yBodyRot = packet.getYHeadRot();
		this.yHeadRot = packet.getYHeadRot();
		this.yBodyRotO = this.yBodyRot;
		this.yHeadRotO = this.yHeadRot;
		this.setId(packet.getId());
		this.setUUID(packet.getUUID());
		this.absMoveTo(d, e, f, 0, 0);
		this.setDeltaMovement(packet.getXa(), packet.getYa(), packet.getZa());
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.pitch = compound.getFloat("TumblePitch");
		this.roll = compound.getFloat("TumbleRoll");
		if (compound.contains("AsteroidState")) {
			this.setState(State.valueOf(compound.getString("AsteroidState")));
		}
		this.setScale(compound.getFloat("Scale"));
		this.ticksSinceActive = compound.getInt("TicksSinceActive");
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("TumblePitch", this.pitch);
		compound.putFloat("TumbleRoll", this.roll);
		compound.putString("AsteroidState", this.getState().name());
		compound.putFloat("Scale", this.getScale());
		compound.putInt("TicksSinceActive", this.ticksSinceActive);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SCALE, 1.0F);
		this.entityData.define(STATE, State.IDLE);
	}

	@Override
	public void die(@NotNull DamageSource damageSource) {
		super.die(damageSource);
		if (damageSource.getDirectEntity() instanceof Player) {
			if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) && !damageSource.isCreativePlayer()) {
				if (isSilkTouchOrShears(damageSource)) {
					//this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(RegisterBlocks.TUMBLEWEED)));
				}
			}
		}
		this.destroy(true);
	}

	public void spawnBreakParticles() {
		if (this.level instanceof ServerLevel level) {
			level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), this.getX(), this.getY(0.6666666666666666D), this.getZ(), 90, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.5D);
		}
	}

	public void spawnFlameParticles() {
		if (this.level instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.SMALL_FLAME, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 15, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
			level.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 15, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	public void spawnSmokeParticles() {
		if (this.level instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 15, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	public void spawnExtinguishParticles() {
		if (this.level instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
			level.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	@Override
	protected void createWitherRose(@Nullable LivingEntity entitySource) {
	}

	@Override
	protected void playSwimSound(float volume) {
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return NonNullList.withSize(1, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.LEFT;
	}

	public static enum State {
		FALLING,
		NO_GRAV,
		IDLE;
	}
}
