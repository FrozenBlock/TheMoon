package net.frozenblock.themoon.entity.data;

import net.frozenblock.themoon.entity.Asteroid;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class TheMoonEntityDataSerializers {
	public static final EntityDataSerializer<Asteroid.State> ASTEROID_STATE = EntityDataSerializer.simpleEnum(Asteroid.State.class);

	public static void init() {
		EntityDataSerializers.registerSerializer(ASTEROID_STATE);
	}
}
