package net.frozenblock.themoon.entity;

import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.themoon.entity.data.TheMoonEntityDataSerializers;
import net.frozenblock.themoon.entity.spawn.AsteroidSpawner;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Asteroid extends Mob {
	public static final boolean FALLING_ASTEROIDS_EFFECTED_BY_ASTEROID_BELTS = false;

	public float prevPitch;
	public float prevRoll;
	public float pitch;
	public float roll;
	public int ticksSinceActive;
	public int ticksInAsteroidBelt;
	public boolean alreadyFell = true;
	public double trueFallDistance;

	public double fallX;
	public double fallZ;
	public boolean isInAsteroidBelt;

	private static final EntityDataAccessor<State> STATE = SynchedEntityData.defineId(Asteroid.class, TheMoonEntityDataSerializers.ASTEROID_STATE);
	private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(Asteroid.class, EntityDataSerializers.FLOAT);

    public Asteroid(EntityType<Asteroid> entityType, Level level) {
		super(entityType, level);
		this.blocksBuilding = true;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityNbt) {
		if (spawnReason == MobSpawnType.COMMAND) {
			this.setScale(1.0F);
		}
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
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

	public double horizontalDistanceTo(double x, double z) {
		double d = this.getX() - x;
		double f = this.getZ() - z;
		return Math.sqrt(d * d + f * f);
	}

	@Override
	protected void doPush(@NotNull Entity entity) {
		if (this.getState() == State.FALLING || this.getBoundingBox().getSize() > entity.getBoundingBox().getSize()) {
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
		if (this.getBlockY() <= this.level.getMinBuildHeight() - 128) {
			this.destroy(false);
			return;
		}
		Vec3 deltaMovement = this.getDeltaMovement();
		this.fallX = deltaMovement.x();
		this.fallZ = deltaMovement.z();
		super.tick();
		if (this.isInAsteroidBelt) {
			this.ticksInAsteroidBelt += 1;
			this.alreadyFell = false;
		} else {
			this.ticksInAsteroidBelt = 0;
		}
		deltaMovement = this.getDeltaMovement();
		if (this.wasTouchingWater && this.getState() == State.FALLING) {
			this.extinguishFire();
			this.setState(State.IDLE);
		}
		float rotationAmount = 10F;
		Vec3 deltaPosTest = this.getDeltaPos();
		Vec3 deltaPos = new Vec3(
				Math.abs(deltaPosTest.x()),
				Math.abs(deltaPosTest.y()),
				Math.abs(deltaPosTest.z())
		);
		if (this.onGround || this.isInAsteroidBelt) {
			this.trueFallDistance = 0;
		} else if (this.getState() != State.NO_GRAV) {
			this.trueFallDistance += deltaPos.y();
		}
		if (deltaPosTest.horizontalDistance() != 0) {
			this.setYRot(-((float) (Mth.atan2(deltaPosTest.x * 10, deltaPosTest.z * 10) * 57.2957763671875D) - 90));
		}
		this.prevPitch = this.pitch;
		this.prevRoll = this.roll;
		float yRotAmount = (float) ((deltaPos.y * 0.5F) * rotationAmount);
		this.pitch += deltaPosTest.z * rotationAmount;
		this.roll += deltaPosTest.x * rotationAmount;
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
			this.noActionTime = 0;
			this.ticksSinceActive = 0;
			this.alreadyFell = true;
			this.spawnSmokeParticles();
			if (this.getRemainingFireTicks() > 0) {
				this.spawnFlameParticles();
			}
			this.setRemainingFireTicks(200);
			double xToUse = Math.abs(fallX) > Math.abs(deltaMovement.x()) ? fallX : deltaMovement.x();
			double zToUse = Math.abs(fallZ) > Math.abs(deltaMovement.z()) ? fallZ : deltaMovement.z();
			double y = deltaMovement.y();
			this.setDeltaMovement(xToUse, y, zToUse);
			if (FALLING_ASTEROIDS_EFFECTED_BY_ASTEROID_BELTS && this.isInAsteroidBelt && Math.abs(y) < 0.075) {
				this.setState(State.IDLE);
				this.extinguishFire();
			}
		} else if (this.getState() == State.NO_GRAV) {
			this.ticksSinceActive = 0;
			if (this.level instanceof ServerLevel serverLevel) {
				WindManager windManager = WindManager.getWindManager(serverLevel);
				Vec3 wind = windManager.getWindMovement3D(this.position(), 5, windClamp, 0.2);
				double windX = wind.x();
				double windY = wind.y() * 0.75;
				double windZ = wind.z();
				deltaMovement = deltaMovement.add((windX * 0.015), (windY * 0.015), (windZ * 0.015));
				this.setDeltaMovement(deltaMovement);
				this.spawnSmokeParticles();
				if (!this.isInAsteroidBelt) {
					this.setState(State.IDLE);
				}
			}
		} else {
			if (this.isInAsteroidBelt) {
				if (this.level instanceof ServerLevel serverLevel) {
					if (AsteroidSpawner.getNoGravAsteroids(serverLevel) < this.getType().getCategory().getMaxInstancesPerChunk() && this.ticksInAsteroidBelt > 100 * this.getScale()) {
						this.ticksSinceActive = 0;
						this.setState(State.NO_GRAV);
					}
				}
			} else if (this.trueFallDistance > this.getScale() && !this.alreadyFell) {
				this.trueFallDistance = 0;
				this.setState(State.FALLING);
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
	}

	public boolean isPlayerWithin(double d) {
		Player closestPlayer = this.level.getNearestPlayer(this, -1.0);
		if (closestPlayer == null) {
			return false;
		}
		return closestPlayer.distanceTo(this) <= d;
	}

	public void destroy(boolean killed) {
		if (this.isAlive()) {
			this.playSound(SoundEvents.ANVIL_PLACE, this.getSoundVolume(), this.getVoicePitch());
		}
		this.spawnBreakParticles();
		if (killed) {
			this.remove(RemovalReason.KILLED);
		} else {
			this.discard();
		}
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
		return source.is(DamageTypeTags.WITCH_RESISTANT_TO) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.FREEZE) || source.is(DamageTypes.SWEET_BERRY_BUSH) || source.is(DamageTypes.WITHER) || source.is(DamageTypes.ARROW) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE)
				|| source.is(DamageTypes.STARVE) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.STING) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FIREBALL) || source.is(DamageTypes.UNATTRIBUTED_FIREBALL) || source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC) || super.isInvulnerableTo(source);
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
		if (this.getState() == State.FALLING) {
			this.level.explode(this, this.getX(), this.getEyeY(), this.getZ(), Math.min(this.getScale() * 3, 10), Level.ExplosionInteraction.TNT);
			this.destroy(false);
		}
		return true;
	}

	@Override
	protected float getWaterSlowDown() {
		if (this.getState() == State.FALLING) {
			return 1F;
		}
		return 0.8F / Mth.clamp(this.getScale(), 1F, 2.25F);
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
		super.knockback(strength / scale, x, z);
	}

	@Override
	public void checkDespawn() {
		if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
			this.discard();
			return;
		}
		if (this.isPersistenceRequired() || this.requiresCustomPersistence()) {
			this.noActionTime = 0;
			return;
		}
		Player entity = this.level.getNearestPlayer(this, -1.0);
		if (entity != null) {
			int i;
			double d = this.horizontalDistanceTo(entity.getX(), entity.getZ());
			d *= d;
			boolean falling = this.getState() == State.FALLING;
			if (!falling && ((d > (double)((i = this.getType().getCategory().getDespawnDistance()) * i) && this.removeWhenFarAway(d)))) {
				this.discard();
			}
			int k = 48;
			int l = k * k;
			if (!falling && (this.noActionTime > 600 && this.random.nextInt(800) == 0 && ((d > (double)l && this.removeWhenFarAway(d))))) {
				this.discard();
			} else if (d < (double)l) {
				this.noActionTime = 0;
			}
		}
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
		if (compound.contains("AsteroidScale")) {
			this.setScale(compound.getFloat("AsteroidScale"));
		}
		this.ticksSinceActive = compound.getInt("TicksSinceActive");
		this.ticksInAsteroidBelt = compound.getInt("TicksInAsteroidBelt");
		this.trueFallDistance = compound.getDouble("TrueFallDistance");
		if (compound.contains("AlreadyFell")) {
			this.alreadyFell = compound.getBoolean("AlreadyFell");
		}
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("TumblePitch", this.pitch);
		compound.putFloat("TumbleRoll", this.roll);
		compound.putString("AsteroidState", this.getState().name());
		compound.putFloat("AsteroidScale", this.getScale());
		compound.putInt("TicksSinceActive", this.ticksSinceActive);
		compound.putInt("TicksInAsteroidBelt", this.ticksInAsteroidBelt);
		compound.putDouble("TrueFallDistance", this.trueFallDistance);
		compound.putBoolean("AlreadyFell", this.alreadyFell);
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
			//level.sendParticles(ParticleTypes.SMALL_FLAME, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 15, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
			//level.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 15, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	public void spawnSmokeParticles() {
		if (this.level instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 2, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	public void spawnExtinguishParticles() {
		if (this.level instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
			level.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(0.6666666666666666D), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	@Override
	protected void playEntityOnFireExtinguishedSound() {
		this.spawnExtinguishParticles();
		super.playEntityOnFireExtinguishedSound();
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
