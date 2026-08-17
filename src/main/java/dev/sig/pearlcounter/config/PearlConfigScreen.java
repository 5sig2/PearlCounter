package dev.sig.pearlcounter.config;

import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.compat.TotemCounterCompat;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.screens.Screen;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.IntSliderOption;
import net.uku3lig.ukulib.config.option.ScreenOpenButton;
import net.uku3lig.ukulib.config.option.SimpleButton;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.option.widget.ButtonTab;
import net.uku3lig.ukulib.config.screen.TabbedConfigScreen;

public final class PearlConfigScreen extends TabbedConfigScreen<PearlCounterConfig> {
    private AbstractWidget swapIntervalWidget;

    public PearlConfigScreen(Screen parent) {
        super("pearlcounter:config.title", parent, PearlCounter.getManager());
    }

    @Override
    protected Tab[] getTabs(PearlCounterConfig config) {
        if (TotemCounterCompat.isAvailable()) {
            return new Tab[]{new CounterTab(), new DisplayTab(), new IntegrationTab()};
        }
        return new Tab[]{new CounterTab(), new DisplayTab()};
    }

    private final class CounterTab extends ButtonTab<PearlCounterConfig> {
        private CounterTab() {
            super("pearlcounter:config.tab.counter", PearlConfigScreen.this.manager);
        }

        @Override
        public WidgetCreator[] getWidgets(PearlCounterConfig config) {
            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("pearlcounter:config.counter_enabled", config.isCounterEnabled(), config::setCounterEnabled),
                    CyclingOption.ofBoolean("pearlcounter:config.nametag_enabled", config.isNametagEnabled(), config::setNametagEnabled),
                    CyclingOption.ofBoolean("pearlcounter:config.separator", config.isSeparator(), config::setSeparator),
                    CyclingOption.ofBoolean("pearlcounter:config.counter_colors", config.isCounterColors(), config::setCounterColors),
                    CyclingOption.ofBoolean("pearlcounter:config.show_in_tab", config.isShowInTab(), config::setShowInTab),
                    CyclingOption.ofBoolean("pearlcounter:config.reset_on_death", config.isResetOnDeath(), config::setResetOnDeath),
                    new SimpleButton("pearlcounter:config.reset_counts", button -> PearlCounter.resetPearlCounter())
            };
        }
    }

    private final class DisplayTab extends ButtonTab<PearlCounterConfig> {
        private DisplayTab() {
            super("pearlcounter:config.tab.display", PearlConfigScreen.this.manager);
        }

        @Override
        public WidgetCreator[] getWidgets(PearlCounterConfig config) {
            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("pearlcounter:config.display_enabled", config.isDisplayEnabled(), config::setDisplayEnabled),
                    new ScreenOpenButton("pearlcounter:config.position", parent -> new PearlPositionSelectScreen(parent, config)),
                    CyclingOption.ofBoolean("pearlcounter:config.default_pearl_icon", config.isUseDefaultPearlIcon(), config::setUseDefaultPearlIcon),
                    CyclingOption.ofBoolean("pearlcounter:config.display_colors", config.isDisplayColors(), config::setDisplayColors),
                    CyclingOption.ofBoolean("pearlcounter:config.colored_xp_bar", config.isColoredXpBar(), config::setColoredXpBar),
                    CyclingOption.ofBoolean("pearlcounter:config.always_show_bar", config.isAlwaysShowBar(), config::setAlwaysShowBar),
                    CyclingOption.ofBoolean("pearlcounter:config.show_use_counter", config.isShowUseCounter(), config::setShowUseCounter)
            };
        }
    }

    private final class IntegrationTab extends ButtonTab<PearlCounterConfig> {
        private IntegrationTab() {
            super("pearlcounter:config.tab.integration", PearlConfigScreen.this.manager);
        }

        @Override
        public WidgetCreator[] getWidgets(PearlCounterConfig config) {
            WidgetCreator swapInterval = (x, y, width, height) -> {
                AbstractWidget widget = new IntSliderOption("pearlcounter:config.swap_seconds", config.getSwapSeconds(), config::setSwapSeconds,
                        seconds -> net.minecraft.network.chat.Component.translatable("pearlcounter:config.seconds", seconds), 1, 30, 1)
                        .createWidget(x, y, width, height);
                widget.active = config.isSharedDisplayEnabled();
                widget.visible = config.isSharedDisplayEnabled();
                PearlConfigScreen.this.swapIntervalWidget = widget;
                return widget;
            };

            return new WidgetCreator[]{
                    CyclingOption.ofBoolean("pearlcounter:config.integration_enabled", config.isIntegrationEnabled(), config::setIntegrationEnabled),
                    CyclingOption.ofBoolean("pearlcounter:config.shared_display", config.isSharedDisplayEnabled(), enabled -> {
                        config.setSharedDisplayEnabled(enabled);
                        if (PearlConfigScreen.this.swapIntervalWidget != null) {
                            PearlConfigScreen.this.swapIntervalWidget.active = enabled;
                            PearlConfigScreen.this.swapIntervalWidget.visible = enabled;
                        }
                    }),
                    CyclingOption.ofBoolean("pearlcounter:config.skip_empty", config.isSkipEmpty(), config::setSkipEmpty),
                    swapInterval
            };
        }
    }
}
