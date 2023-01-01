package com.arcanewarrior.disguises;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Disguise extends LivingEntity {

    private final DisguiseManager manager;
    private final Player player;

    public Disguise(@NotNull EntityType entityType, @NotNull Player player, @NotNull DisguiseManager manager) {
        super(entityType);
        this.player = player;
        this.manager = manager;
        if(MinestomDisguises.getInstance().getConfiguration().getTable("disguises").getBoolean("translate-player-names", true)) {
            setCustomName(Component.text(player.getUsername()));
            setCustomNameVisible(true);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void undisguise() {
       manager.undisguisePlayer(getPlayer());
    }
}
