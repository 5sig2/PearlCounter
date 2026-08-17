package dev.sig.pearlcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PlayerTabOverlay.class, priority = 2000)
public abstract class MixinPlayerTabOverlay {
    @ModifyReturnValue(method = "getNameForDisplay", at = @At("RETURN"))
    private Component pearlcounter$appendTabCounter(Component original, @Local(argsOnly = true) PlayerInfo info) {
        if (!PearlCounterConfig.get().isShowInTab() || Minecraft.getInstance().level == null) return original;
        Player player = Minecraft.getInstance().level.getPlayerByUUID(info.getProfile().id());
        return player == null ? original : PearlCounter.appendCounterForTab(player, original);
    }
}
