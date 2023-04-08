package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.world.generation.saved.crater.SavedCraterManager;
import net.frozenblock.themoon.world.generation.saved.crater.impl.SavedCraterManagerInterface;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements SavedCraterManagerInterface {

	@Unique
	private final SavedCraterManager theMoon$savedCraterManager = new SavedCraterManager();

	@Unique
	public SavedCraterManager theMoon$getSavedCraterManager() {
		return this.theMoon$savedCraterManager;
	}

}
