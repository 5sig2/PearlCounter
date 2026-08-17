package dev.sig.pearlcounter.mixin;

import dev.sig.pearlcounter.compat.IntegrationState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 2000)
public abstract class MixinGuiIntegrationStart {
    @Inject(method = "renderPlayerHealth", at = @At("HEAD"))
    private void pearlcounter$beginCombinedRender(GuiGraphics graphics, CallbackInfo ci) {
        IntegrationState.beginHudRender();
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void pearlcounter$endCombinedRender(CallbackInfo ci) {
        IntegrationState.endHudRender();
    }
}
