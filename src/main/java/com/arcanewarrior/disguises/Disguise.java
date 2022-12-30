package com.arcanewarrior.disguises;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Disguise extends LivingEntity {

    private final Player player;

    public Disguise(@NotNull EntityType entityType, @NotNull Player player) {
        super(entityType);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void unDisguise() {
        MinestomDisguises.getInstance().getDisguiseManager().undisguisePlayer(getPlayer());
    }
}
