package dev.sig.pearlcounter.mixin;

import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.player.Player;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = DisplayRenderer.TextDisplayRenderer.class, priority = 2000)
public abstract class MixinTextDisplayRenderer {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Display$TextDisplay;Lnet/minecraft/client/renderer/entity/state/TextDisplayEntityRenderState;F)V", at = @At("RETURN"))
    private void pearlcounter$appendTextDisplay(Display.TextDisplay entity, TextDisplayEntityRenderState state,
                                                float partialTick, CallbackInfo ci) {
        if (!PearlCounterConfig.get().isCounterEnabled() || !PearlCounterConfig.get().isNametagEnabled()) return;
        if (state.cachedInfo == null || !(entity.getVehicle() instanceof Player player)) return;

        List<Display.TextDisplay.CachedLine> lines = state.cachedInfo.lines();
        for (int i = 0; i < lines.size(); i++) {
            Display.TextDisplay.CachedLine line = lines.get(i);
            Component original = Ukutils.getStyledText(line.contents());
            if (original.getString().isBlank() || !original.getString().contains(player.getScoreboardName())) continue;

            Component modified = PearlCounter.appendCounter(player, original);
            if (modified == original) return;

            FormattedCharSequence sequence = modified.getVisualOrderText();
            List<Display.TextDisplay.CachedLine> replacement = new ArrayList<>(lines);
            replacement.set(i, new Display.TextDisplay.CachedLine(sequence, Minecraft.getInstance().font.width(modified)));
            int maxWidth = replacement.stream().mapToInt(Display.TextDisplay.CachedLine::width).max().orElse(state.cachedInfo.width());
            state.cachedInfo = new Display.TextDisplay.CachedInfo(replacement, maxWidth);
            return;
        }
    }
}
