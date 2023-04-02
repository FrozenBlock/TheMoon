package net.frozenblock.themoon.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.frozenblock.themoon.registry.TheMoonDimensionSpecialEffects;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionSpecialEffects.class)
public class DimensionSpecialEffectsMixin {

	@Inject(method = "method_29092(Lit/unimi/dsi/fastutil/objects/Object2ObjectArrayMap;)V", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectArrayMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 2, shift = At.Shift.AFTER))
	private static void theMoon$injectMoonEffects(Object2ObjectArrayMap object2ObjectArrayMap, CallbackInfo info) {
		object2ObjectArrayMap.put(TheMoonDimensionSpecialEffects.MOON_EFFECTS, new TheMoonDimensionSpecialEffects.MoonEffects());
	}

}
