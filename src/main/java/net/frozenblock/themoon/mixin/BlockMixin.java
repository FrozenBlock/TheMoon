package net.frozenblock.themoon.mixin;

import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

	@Inject(method = "wasExploded", at = @At("HEAD"), cancellable = true)
	public void theMoon$wasExploded(Level level, BlockPos pos, Explosion explosion, CallbackInfo info) {
		if (!level.isClientSide && Block.class.cast(this).defaultBlockState().is(TheMoonBlockTags.MOON_DUST)) {
			RandomSource randomSource = level.getRandom();
			for (ServerPlayer player : ((ServerLevel) level).players()) {
				((ServerLevel) level).sendParticles(
						player,
						TheMoonParticleTypes.MOON_DUST,
						true,
						pos.getX(),
						pos.getY(),
						pos.getZ(),
						randomSource.nextInt(1, 4),
						1,
						1,
						1,
						0.01
				);
			}
		}
	}

}
