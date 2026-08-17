package dev.sig.pearlcounter.config;

import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.PearlCounterColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;
import net.uku3lig.ukulib.utils.Ukutils;
import org.joml.Vector2ic;

public final class PearlPositionSelectScreen extends PositionSelectScreen {
    private final PearlCounterConfig config;

    protected PearlPositionSelectScreen(Screen parent, PearlCounterConfig config) {
        super("PearlCounter Position", parent, config.getX(), config.getY(), PearlCounter.getManager(), (x, y) -> {
            config.setX(x);
            config.setY(y);
        });
        this.config = config;
    }

    @Override
    protected void draw(GuiGraphics graphics, int mouseX, int mouseY, float delta, int x, int y) {
        Component example = Component.literal(config.isShowUseCounter() ? "-8" : "8");
        if (config.isUseDefaultPearlIcon()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, PearlCounter.DEFAULT_PEARL_TEXTURE, x, y, 0, 0, 16, 16, 16, 16);
        } else {
            graphics.renderItem(PearlCounter.PEARL, x, y);
        }
        Vector2ic coords = Ukutils.getTextCoords(example, width, font, x, y);
        graphics.drawString(font, example, coords.x(), coords.y(), PearlCounterColors.forUses(8));
    }

    @Override
    protected void drawDefault(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        draw(graphics, mouseX, mouseY, delta, width / 2 - 8, height - 38 - font.lineHeight);
    }
}
