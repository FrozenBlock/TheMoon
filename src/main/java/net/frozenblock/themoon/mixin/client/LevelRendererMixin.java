package net.frozenblock.themoon.mixin.client;

import net.frozenblock.themoon.registry.TheMoonParticleTypes;
import net.frozenblock.themoon.tag.TheMoonBlockTags;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow @Nullable
	private ClientLevel level;

	@ModifyArgs(method = "levelEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addDestroyBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 0))
	public void theMoon$moonDustOnBreak(@NotNull Args args) {
		BlockPos pos = args.get(0);
		BlockState state = args.get(1);
		if (state.is(TheMoonBlockTags.MOON_DUST)) {
			assert level != null;
			RandomSource randomSource = level.getRandom();
			int maxParticles = randomSource.nextInt(0, 4);
			for (int j = 0; j < maxParticles; ++j) {
				double addedX = randomSource.nextDouble() * 0.8D + 0.1D;
				double addedY = randomSource.nextDouble() * 0.8D + 0.1D;
				double addedZ = randomSource.nextDouble() * 0.8D + 0.1D;

				double xVel = Mth.lerp((addedX - 0.1) * 1.25, -0.03, 0.03);
				double zVel = Mth.lerp((addedZ - 0.1) * 1.25, -0.03, 0.03);
				this.level.addParticle(
						TheMoonParticleTypes.MOON_DUST,
						(double)pos.getX() + addedX, (double)pos.getY() + addedY, (double)pos.getZ() + addedZ,
						xVel, 0.0075D, zVel
				);
			}
		}
	}

}
