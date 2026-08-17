package dev.sig.pearlcounter;

import dev.sig.pearlcounter.compat.IntegrationState;
import dev.sig.pearlcounter.compat.TotemCounterCompat;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PearlHudRenderer {
    private static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static boolean combinedLastFrame;
    private static boolean pearlPhase;
    private static int lastSwapSeconds = -1;
    private static long lastSwapTick;

    private PearlHudRenderer() {
    }

    public static DisplayEntry activeEntry(Player player) {
        PearlCounterConfig config = PearlCounterConfig.get();
        DisplayEntry pearl = pearlEntry(player, config);
        if (!IntegrationState.shouldCombine()) {
            combinedLastFrame = false;
            return pearl;
        }

        updateSwapTimer(config);

        int totemCount = TotemCounterCompat.getCount(player);
        DisplayEntry totem = new DisplayEntry(TOTEM, false, totemCount, TotemCounterCompat.showsPopCounter(), TotemCounterCompat.getColor(totemCount));

        if (config.isSkipEmpty()) {
            if (pearl.count() == 0) return totem;
            if (totem.count() == 0) return pearl;
        }

        return pearlPhase ? pearl : totem;
    }

    private static void updateSwapTimer(PearlCounterConfig config) {
        long now = PearlCounter.getClientTicks();
        int seconds = Math.max(1, config.getSwapSeconds());
        if (!combinedLastFrame || lastSwapSeconds != seconds) {
            combinedLastFrame = true;
            pearlPhase = true;
            lastSwapSeconds = seconds;
            lastSwapTick = now;
            return;
        }

        long interval = seconds * 20L;
        long elapsed = now - lastSwapTick;
        if (elapsed >= interval) {
            long completedIntervals = elapsed / interval;
            if ((completedIntervals & 1L) == 1L) pearlPhase = !pearlPhase;
            lastSwapTick += completedIntervals * interval;
        }
    }

    private static DisplayEntry pearlEntry(Player player, PearlCounterConfig config) {
        int count = PearlCounter.getDisplayCount(player);
        return new DisplayEntry(PearlCounter.PEARL, config.isUseDefaultPearlIcon(), count, config.isShowUseCounter(), PearlCounter.getDisplayColor(count));
    }

    public record DisplayEntry(ItemStack item, boolean defaultPearlIcon, int count, boolean negative, int color) {
    }
}
