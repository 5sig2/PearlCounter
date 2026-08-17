package dev.sig.pearlcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PearlTracker {
    private static final double MAX_OWNER_DISTANCE_SQUARED = 16.0 * 16.0;
    private static final int OWNER_RESOLUTION_TICKS = 20;

    private final Map<UUID, Integer> uses = new HashMap<>();
    private final Set<UUID> observedProjectiles = new HashSet<>();
    private final List<PendingPearl> pending = new ArrayList<>();
    private ClientLevel currentLevel;
    private long tick;

    public void onPearlSpawn(ThrownEnderpearl pearl) {
        if (pearl.level() instanceof ClientLevel pearlLevel && pearlLevel != currentLevel) {
            currentLevel = pearlLevel;
            clearSilently();
        }
        if (!observedProjectiles.add(pearl.getUUID())) return;

        Entity owner = pearl.getOwner();
        if (owner instanceof Player player) {
            countIfPlausible(player, pearl.position());
        } else {
            pending.add(new PendingPearl(pearl.getId(), pearl.getUUID(), pearl.position(), tick + OWNER_RESOLUTION_TICKS));
        }
    }

    public void tick(Minecraft minecraft) {
        tick++;
        if (minecraft.level != currentLevel) {
            currentLevel = minecraft.level;
            clearSilently();
        }

        if (currentLevel == null || pending.isEmpty()) return;

        Iterator<PendingPearl> iterator = pending.iterator();
        while (iterator.hasNext()) {
            PendingPearl waiting = iterator.next();
            Entity entity = currentLevel.getEntity(waiting.entityId());
            if (entity instanceof ThrownEnderpearl pearl && pearl.getUUID().equals(waiting.projectileUuid())
                    && pearl.getOwner() instanceof Player player) {
                countIfPlausible(player, waiting.spawnPosition());
                iterator.remove();
            } else if (tick >= waiting.expiresAt()) {
                iterator.remove();
            }
        }
    }

    private void countIfPlausible(Player owner, Vec3 spawnPosition) {
        Player local = Minecraft.getInstance().player;
        if (owner == local || owner.distanceToSqr(spawnPosition) <= MAX_OWNER_DISTANCE_SQUARED) {
            uses.merge(owner.getUUID(), 1, Integer::sum);
        }
    }

    public int getUses(Player player) {
        return player == null ? 0 : uses.getOrDefault(player.getUUID(), 0);
    }

    public boolean hasUses(Player player) {
        return player != null && uses.containsKey(player.getUUID());
    }

    public void reset(Player player) {
        if (player != null) uses.remove(player.getUUID());
    }

    public void resetAll() {
        uses.clear();
    }

    private void clearSilently() {
        uses.clear();
        observedProjectiles.clear();
        pending.clear();
    }

    private record PendingPearl(int entityId, UUID projectileUuid, Vec3 spawnPosition, long expiresAt) {
    }
}
