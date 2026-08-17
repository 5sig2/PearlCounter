package dev.sig.pearlcounter;

public final class PearlCounterColors {
    public static final int WHITE = 0xFFFFFFFF;

    private PearlCounterColors() {
    }

    public static int forUses(int uses) {
        if (uses < 8) return 0xFF55FF55;
        if (uses < 16) return 0xFF00AA00;
        if (uses < 24) return 0xFFFFFF55;
        if (uses < 32) return 0xFFFFAA00;
        return 0xFFFF5555;
    }
}
