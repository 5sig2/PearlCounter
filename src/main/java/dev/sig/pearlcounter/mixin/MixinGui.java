package dev.sig.pearlcounter.mixin;

import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.PearlHudRenderer;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.utils.Ukutils;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 500)
public abstract class MixinGui {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderPlayerHealth", at = @At("RETURN"))
    private void pearlcounter$renderCounter(GuiGraphics graphics, CallbackInfo ci) {
        PearlCounterConfig config = PearlCounterConfig.get();
        if (minecraft.player == null || !config.isDisplayEnabled()) return;

        PearlHudRenderer.DisplayEntry entry = PearlHudRenderer.activeEntry(minecraft.player);
        if (entry.count() == 0) return;

        Font font = minecraft.font;
        Component text = Component.literal((entry.negative() ? "-" : "") + entry.count());
        int x = config.getX();
        int y = config.getY();
        if (x == -1 || y == -1) {
            x = graphics.guiWidth() / 2 - 8;
            y = graphics.guiHeight() - 38 - font.lineHeight;
            if (minecraft.player.experienceLevel > 0) y -= 6;
        }

        Vector2ic textCoordinates = Ukutils.getTextCoords(text, graphics.guiWidth(), font, x, y);
        graphics.pose().pushMatrix();
        if (entry.defaultPearlIcon()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, PearlCounter.DEFAULT_PEARL_TEXTURE, x, y, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.renderItem(entry.item(), x, y);
        }
        graphics.drawString(font, text, textCoordinates.x(), textCoordinates.y(), entry.color());
        graphics.pose().popMatrix();
    }
}
