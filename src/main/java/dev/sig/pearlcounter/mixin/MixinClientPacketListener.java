package dev.sig.pearlcounter.mixin;

import dev.sig.pearlcounter.PearlCounter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {
    @Shadow
    private ClientLevel level;

    @Inject(method = "handleAddEntity", at = @At("TAIL"))
    private void pearlcounter$observePearl(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        Entity entity = level.getEntity(packet.getId());
        if (entity instanceof ThrownEnderpearl pearl) {
            PearlCounter.getTracker().onPearlSpawn(pearl);
        }
    }
}
