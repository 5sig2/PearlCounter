package dev.sig.pearlcounter.mixin.compat;

import dev.sig.pearlcounter.compat.IntegrationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.uku3lig.totemcounter.config.TotemCounterConfig", remap = false)
public abstract class MixinTotemCounterConfig {
    @Inject(method = "isDisplayEnabled", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void pearlcounter$suppressSeparateTotemHud(CallbackInfoReturnable<Boolean> cir) {
        if (IntegrationState.shouldSuppressTotemHud()) cir.setReturnValue(false);
    }
}
