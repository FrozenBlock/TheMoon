package net.frozenblock.themoon.world.generation.saved.crater;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import net.frozenblock.themoon.world.generation.saved.crater.impl.SavedCraterManagerInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SavedCraterManager {
	private boolean canRun = false;
	private ArrayList<SavedCrater> craters = new ArrayList<>();

	public SavedCraterManager() {
	}

	public void addCrater(SavedCrater savedCrater) {
		craters.add(savedCrater);
	}

	public ArrayList<SavedCrater> getCraters() {
		return this.craters;
	}

	public void tick(ServerLevel level) {
		if (this.canRun) {
			ArrayList<SavedCrater> savedCraters = (ArrayList<SavedCrater>) this.craters.clone();
			savedCraters.removeIf(savedCrater -> savedCrater.generate(level));
			this.craters = savedCraters;
		}
	}

	public boolean hasCraterAtSamePosAndChunk(BlockPos chunk, BlockPos pos) {
		for (SavedCrater savedCrater : this.getCraters()) {
			if (savedCrater.getBlockPos().equals(pos) && new ChunkPos(savedCrater.getSavedChunkPos()).equals(new ChunkPos(chunk))) {
				return true;
			}
		}
		return false;
	}

	public static SavedCraterManager getSavedCraterManager(ServerLevel level) {
		return ((SavedCraterManagerInterface)level).theMoon$getSavedCraterManager();
	}

	public SavedCraterStorage createData() {
		SavedCraterStorage savedCraterStorage = new SavedCraterStorage(this);
		this.canRun = true;
		return savedCraterStorage;
	}

	public SavedCraterStorage createData(CompoundTag nbt) {
		SavedCraterStorage savedCraterStorage = this.createData().load(nbt);
		this.canRun = true;
		return savedCraterStorage;
	}

	public static class SavedCrater {
		private static final BlockState placeState = Blocks.AIR.defaultBlockState();

		public final BlockPos savedChunkPos;
		public final int depth;
		public final int k;
		public final BlockPos blockPos;
		public final BlockPos blockPos2;

		public static final Codec<SavedCrater> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				BlockPos.CODEC.fieldOf("SavedChunkPos").forGetter(SavedCrater::getSavedChunkPos),
				Codec.INT.fieldOf("Depth").forGetter(SavedCrater::getDepth),
				Codec.INT.fieldOf("K").forGetter(SavedCrater::getK),
				BlockPos.CODEC.fieldOf("BlockPos").forGetter(SavedCrater::getBlockPos),
				BlockPos.CODEC.fieldOf("BlockPos2").forGetter(SavedCrater::getBlockPos2)
		).apply(instance, SavedCrater::new));

		public SavedCrater(BlockPos savedChunkPos, int depth, int k, BlockPos blockPos, BlockPos blockPos2) {
			this.savedChunkPos = savedChunkPos;
			this.depth = depth;
			this.k = k;
			this.blockPos = blockPos;
			this.blockPos2 = blockPos2;
		}

		public boolean generate(ServerLevel level) {
			if (level.isLoaded(this.savedChunkPos)) {
				ChunkPos chunkInside = new ChunkPos(this.savedChunkPos);
				BlockPos.MutableBlockPos mutableBlockPos = this.blockPos.mutable();
				double radius = this.k * this.k;
				int x = this.blockPos.getX();
				int y = this.blockPos.getY();
				int z = this.blockPos.getZ();
				for (int offsetY = -this.depth; offsetY <= this.k; ++offsetY) {
					boolean bl = false;
					for (int offsetX = -this.k; offsetX <= this.k; ++offsetX) {
						for (int offsetZ = -this.k; offsetZ <= this.k; ++offsetZ) {
							mutableBlockPos.set(x + offsetX, y + offsetY, z + offsetZ);
							if (mutableBlockPos.distSqr(this.blockPos2) < radius) {
								bl = true;
								if (new ChunkPos(mutableBlockPos).equals(chunkInside)) {
									level.setBlock(mutableBlockPos, placeState, 3);
								}
							}
						}
					}

					if (!bl && offsetY > 0) {
						break;
					}
				}
				return true;
			}
			return false;
		}

		public BlockPos getSavedChunkPos() {
			return this.savedChunkPos;
		}

		public int getDepth() {
			return this.depth;
		}

		public int getK() {
			return this.k;
		}

		public BlockPos getBlockPos() {
			return this.blockPos;
		}

		public BlockPos getBlockPos2() {
			return this.blockPos2;
		}
	}

}
