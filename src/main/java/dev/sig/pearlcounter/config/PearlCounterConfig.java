package dev.sig.pearlcounter.config;

import dev.sig.pearlcounter.PearlCounter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PearlCounterConfig implements Serializable {
    private boolean counterEnabled = true;
    private boolean nametagEnabled = true;
    private boolean separator = false;
    private boolean counterColors = true;
    private boolean showInTab = false;
    private boolean resetOnDeath = true;

    private boolean displayEnabled = true;
    private int x = -1;
    private int y = -1;
    private boolean useDefaultPearlIcon = false;
    private boolean displayColors = true;
    private boolean coloredXpBar = false;
    private boolean alwaysShowBar = false;
    private boolean showUseCounter = false;

    private boolean integrationEnabled = true;
    private boolean sharedDisplayEnabled = true;
    private int swapSeconds = 5;
    private boolean skipEmpty = true;

    public static PearlCounterConfig get() {
        return PearlCounter.getManager().getConfig();
    }

    public int getSwapSeconds() {
        return swapSeconds <= 0 ? 5 : Math.min(swapSeconds, 30);
    }

    public void setSwapSeconds(int swapSeconds) {
        this.swapSeconds = Math.max(1, Math.min(swapSeconds, 30));
    }
}
