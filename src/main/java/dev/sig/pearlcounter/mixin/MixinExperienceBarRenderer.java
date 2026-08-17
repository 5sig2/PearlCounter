package dev.sig.pearlcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.PearlHudRenderer;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
import net.minecraft.resources.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExperienceBarRenderer.class)
public abstract class MixinExperienceBarRenderer {
    @Unique
    private PearlHudRenderer.DisplayEntry pearlcounter$entry() {
        return PearlHudRenderer.activeEntry(Minecraft.getInstance().player);
    }

    @Unique
    private boolean pearlcounter$shouldColor() {
        return PearlCounterConfig.get().isColoredXpBar() && pearlcounter$entry().count() != 0;
    }

    @ModifyExpressionValue(method = "renderBackground", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;experienceProgress:F", opcode = Opcodes.GETFIELD))
    private float pearlcounter$changeProgress(float original) {
        return pearlcounter$shouldColor() && PearlCounterConfig.get().isAlwaysShowBar() ? 1.0F : original;
    }

    @WrapOperation(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"))
    private void pearlcounter$colorProgress(GuiGraphics graphics, RenderPipeline pipeline, Identifier sprite,
                                            int textureWidth, int textureHeight, int u, int v, int x, int y,
                                            int width, int height, Operation<Void> original) {
        if (pearlcounter$shouldColor()) {
            PearlHudRenderer.DisplayEntry entry = pearlcounter$entry();
            graphics.blit(pipeline, PearlCounter.WHITE_BAR, x, y, 0, 0, width, 5, 182, 5, entry.color());
        } else {
            original.call(graphics, pipeline, sprite, textureWidth, textureHeight, u, v, x, y, width, height);
        }
    }
}
