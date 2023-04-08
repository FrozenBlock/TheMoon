package net.frozenblock.themoon.world.generation.saved.crater;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class SavedCraterStorage extends SavedData {
	public static final String CRATER_FILE_ID = "the_moon_craters";
	private final SavedCraterManager savedCraterManager;

	public SavedCraterStorage(SavedCraterManager savedCraterManager) {
		this.savedCraterManager = savedCraterManager;
		this.setDirty();
	}

	@Override
	public CompoundTag save(@NotNull CompoundTag compoundTag) {
		Logger logger = TheMoonSharedConstants.LOGGER;
		DataResult<Tag> var10000 = SavedCraterManager.SavedCrater.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.savedCraterManager.getCraters());
		Objects.requireNonNull(logger);
		var10000.resultOrPartial(logger::error).ifPresent((nbt) -> compoundTag.put("SavedCraters", nbt));

		TheMoonSharedConstants.log("Saving Crater data.", TheMoonSharedConstants.UNSTABLE_LOGGING);

		return compoundTag;
	}

	public SavedCraterStorage load(CompoundTag compoundTag) {
		if (compoundTag.contains("SavedCraters", 9)) {
			this.savedCraterManager.getCraters().clear();
			DataResult<List<SavedCraterManager.SavedCrater>> var10000 = SavedCraterManager.SavedCrater.CODEC.listOf().parse(new Dynamic<>(NbtOps.INSTANCE, compoundTag.getList("SavedCraters", 10)));
			Logger logger = TheMoonSharedConstants.LOGGER;
			Objects.requireNonNull(logger);
			Optional<List<SavedCraterManager.SavedCrater>> list = var10000.resultOrPartial(logger::error);
			if (list.isPresent()) {
				List<SavedCraterManager.SavedCrater> craters = list.get();
				this.savedCraterManager.getCraters().addAll(craters);
			}
		}

		TheMoonSharedConstants.log("Loading Crater data.", TheMoonSharedConstants.UNSTABLE_LOGGING);

		return this;
	}
}
