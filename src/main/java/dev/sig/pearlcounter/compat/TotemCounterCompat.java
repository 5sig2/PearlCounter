package dev.sig.pearlcounter.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;

public final class TotemCounterCompat {
    private static final Method GET_COUNT;
    private static final Method GET_COLOR;
    private static final Method GET_CONFIG;
    private static final Method SHOW_POP_COUNTER;
    private static final boolean AVAILABLE;

    static {
        Method getCount = null;
        Method getColor = null;
        Method getConfig = null;
        Method showPopCounter = null;
        boolean available = false;

        if (FabricLoader.getInstance().isModLoaded("totemcounter")) {
            try {
                Class<?> counter = Class.forName("net.uku3lig.totemcounter.TotemCounter");
                Class<?> config = Class.forName("net.uku3lig.totemcounter.config.TotemCounterConfig");
                getCount = counter.getMethod("getCount", Player.class);
                getColor = counter.getMethod("getColor", int.class);
                getConfig = config.getMethod("get");
                showPopCounter = config.getMethod("isShowPopCounter");
                available = true;
            } catch (ReflectiveOperationException ignored) {
                // Compatibility is optional. A changed TotemCounter API falls back to separate rendering.
            }
        }

        GET_COUNT = getCount;
        GET_COLOR = getColor;
        GET_CONFIG = getConfig;
        SHOW_POP_COUNTER = showPopCounter;
        AVAILABLE = available;
    }

    private TotemCounterCompat() {
    }

    public static boolean isAvailable() {
        return AVAILABLE;
    }

    public static int getCount(Player player) {
        if (!AVAILABLE || player == null) return 0;
        try {
            return (int) GET_COUNT.invoke(null, player);
        } catch (ReflectiveOperationException ignored) {
            return 0;
        }
    }

    public static int getColor(int count) {
        if (!AVAILABLE) return 0xFFFFFFFF;
        try {
            return (int) GET_COLOR.invoke(null, count);
        } catch (ReflectiveOperationException ignored) {
            return 0xFFFFFFFF;
        }
    }

    public static boolean showsPopCounter() {
        if (!AVAILABLE) return false;
        try {
            Object config = GET_CONFIG.invoke(null);
            return (boolean) SHOW_POP_COUNTER.invoke(config);
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
}
