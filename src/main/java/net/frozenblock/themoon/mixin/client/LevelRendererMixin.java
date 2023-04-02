package net.frozenblock.themoon.mixin.client;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.frozenblock.themoon.util.TheMoonUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Unique
	private static final ResourceLocation theMoon$MOON_LOCATION = TheMoonSharedConstants.id("textures/environment/earth_phases.png");

	@Shadow
	@Nullable
	private ClientLevel level;

	@ModifyArgs(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1))
	public void theMoon$setGravity(Args args) {
		if (TheMoonUtils.isOuterSpace(this.level)) {
			args.set(1, theMoon$MOON_LOCATION);
		}
	}

}
