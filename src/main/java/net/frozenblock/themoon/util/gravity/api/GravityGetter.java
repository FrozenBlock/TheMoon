package net.frozenblock.themoon.util.gravity.api;

import net.frozenblock.themoon.util.gravity.impl.LevelGravityInterface;
import net.minecraft.world.level.Level;

public class GravityGetter {

	public static double getGravity(Level level) {
		return ((LevelGravityInterface)level).getGravity();
	}

}
