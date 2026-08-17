package dev.sig.pearlcounter.compat;

import dev.sig.pearlcounter.config.PearlCounterConfig;

public final class IntegrationState {
    private static boolean suppressTotemHud;

    private IntegrationState() {
    }

    public static boolean shouldCombine() {
        PearlCounterConfig config = PearlCounterConfig.get();
        return config.isDisplayEnabled()
                && config.isIntegrationEnabled()
                && config.isSharedDisplayEnabled()
                && TotemCounterCompat.isAvailable();
    }

    public static void beginHudRender() {
        suppressTotemHud = shouldCombine();
    }

    public static void endHudRender() {
        suppressTotemHud = false;
    }

    public static boolean shouldSuppressTotemHud() {
        return suppressTotemHud;
    }
}
