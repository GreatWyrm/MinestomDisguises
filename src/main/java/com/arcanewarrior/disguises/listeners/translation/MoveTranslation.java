package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerMoveEvent;

public final class MoveTranslation {
    public static void listener(PlayerMoveEvent event, DisguiseManager parentManager) {
        Player player = event.getPlayer();
        Disguise disguise = parentManager.getPlayerDisguise(player);
        if (disguise != null) {
            disguise.setPose(player.getPose());
            if(player.getVehicle() == null)
                disguise.teleport(new Pos(event.getNewPosition()).withYaw(player.getPosition().yaw()).withPitch(player.getPosition().pitch()));
            else
                disguise.setView(event.getNewPosition().yaw(), event.getNewPosition().pitch());
        }
    }
}
