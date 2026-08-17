package dev.sig.pearlcounter;

import dev.sig.pearlcounter.config.PearlConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.function.UnaryOperator;

public final class UkulibHook implements UkulibAPI {
    @Override
    public UnaryOperator<Screen> supplyConfigScreen() {
        return PearlConfigScreen::new;
    }
}
