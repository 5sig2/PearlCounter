package dev.sig.pearlcounter;

import com.mojang.blaze3d.platform.InputConstants;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.utils.Ukutils;
import org.lwjgl.glfw.GLFW;

import java.util.stream.Stream;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class PearlCounter implements ClientModInitializer {
    public static final String MOD_ID = "pearlcounter";
    public static final char PEARL_GLYPH = '\u25CF';
    private static final char NEGATIVE_SPACE_GLYPH = '\uE000';
    public static final ItemStack PEARL = new ItemStack(Items.ENDER_PEARL);
    public static final Identifier DEFAULT_PEARL_TEXTURE = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/default_pearl.png");
    public static final Identifier WHITE_BAR = Identifier.fromNamespaceAndPath(MOD_ID, "gui/bar.png");
    private static final FontDescription PEARL_FONT = new FontDescription.Resource(Identifier.fromNamespaceAndPath(MOD_ID, "pearl"));

    @Getter
    private static final ConfigManager<PearlCounterConfig> manager = ConfigManager.createDefault(PearlCounterConfig.class, MOD_ID);
    @Getter
    private static final PearlTracker tracker = new PearlTracker();
    @Getter
    private static long clientTicks;

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "key"));
    private static final KeyMapping OPEN_CONFIG = new KeyMapping("pearlcounter:key.open_config", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F9, CATEGORY);

    @Override
    public void onInitializeClient() {
        Ukutils.registerKeybinding(OPEN_CONFIG, client -> client.setScreen(new dev.sig.pearlcounter.config.PearlConfigScreen(client.screen)));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            clientTicks++;
            tracker.tick(client);
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(literal("resetpearls").executes(context -> {
                    resetPearlCounter();
                    context.getSource().sendFeedback(Component.literal("PearlCounter: pearl counts reset."));
                    return 1;
                }))
        );
    }

    public static int getInventoryCount(Player player) {
        if (player == null) return 0;
        Inventory inventory = player.getInventory();
        ItemStack offhand = inventory.getItem(Inventory.SLOT_OFFHAND);
        return Stream.concat(inventory.getNonEquipmentItems().stream(), Stream.of(offhand))
                .filter(stack -> stack.is(Items.ENDER_PEARL))
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    public static int getDisplayCount(Player player) {
        return PearlCounterConfig.get().isShowUseCounter() ? tracker.getUses(player) : getInventoryCount(player);
    }

    public static int getDisplayColor(int count) {
        return PearlCounterConfig.get().isDisplayColors() ? PearlCounterColors.forUses(count) : PearlCounterColors.WHITE;
    }

    public static Component appendCounter(Player player, Component original) {
        return appendCounter(player, original, true);
    }

    public static Component appendCounterForTab(Player player, Component original) {
        return appendCounter(player, original, false);
    }

    private static Component appendCounter(Player player, Component original, boolean requireNametagEnabled) {
        PearlCounterConfig config = PearlCounterConfig.get();
        if (!config.isCounterEnabled() || (requireNametagEnabled && !config.isNametagEnabled())
                || !tracker.hasUses(player) || original.getString().indexOf(PEARL_GLYPH) >= 0) return original;

        int uses = tracker.getUses(player);
        MutableComponent result = original.copy().append(" ");
        if (config.isSeparator()) result.append(Component.literal("| ").withStyle(style -> style.withColor(0xAAAAAA)));

        MutableComponent count = Component.literal("-" + uses);
        if (config.isCounterColors()) count.setStyle(Style.EMPTY.withColor(PearlCounterColors.forUses(uses)));

        MutableComponent icon = Component.literal("" + PEARL_GLYPH + NEGATIVE_SPACE_GLYPH)
                .withStyle(Style.EMPTY.withFont(PEARL_FONT).withColor(PearlCounterColors.WHITE));
        return result.append(count).append(icon);
    }

    public static void resetPearlCounter() {
        tracker.resetAll();
        Ukutils.sendToast(Component.translatable("pearlcounter:toast.reset.title"), Component.translatable("pearlcounter:toast.reset.description"));
    }
}
