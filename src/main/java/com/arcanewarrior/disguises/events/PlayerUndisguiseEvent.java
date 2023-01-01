package com.arcanewarrior.disguises.events;

import com.arcanewarrior.disguises.Disguise;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerUndisguiseEvent implements CancellableEvent, PlayerEvent {

    private final Player player;
    private final Disguise disguise;
    private boolean cancelled;

    public PlayerUndisguiseEvent(Player player, Disguise disguise) {
        this.player = player;
        this.disguise = disguise;
        this.cancelled = false;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public Disguise getDisguise() {
        return disguise;
    }
}
