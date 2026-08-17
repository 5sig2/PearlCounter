package dev.sig.pearlcounter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.sig.pearlcounter.PearlCounter;
import dev.sig.pearlcounter.config.PearlCounterConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 2000)
public abstract class MixinPlayer {
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    private Component pearlcounter$appendNametag(Component original) {
        return PearlCounter.appendCounter((Player) (Object) this, original);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void pearlcounter$resetOnDeath(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!player.isAlive() && PearlCounterConfig.get().isResetOnDeath()) {
            PearlCounter.getTracker().reset(player);
        }
    }
}
