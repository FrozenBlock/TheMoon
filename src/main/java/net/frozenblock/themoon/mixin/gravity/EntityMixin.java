package net.frozenblock.themoon.mixin.gravity;

import net.frozenblock.themoon.util.gravity.api.GravityGetter;
import net.frozenblock.themoon.util.gravity.impl.EntityGravityInterface;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class EntityMixin implements EntityGravityInterface {

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
