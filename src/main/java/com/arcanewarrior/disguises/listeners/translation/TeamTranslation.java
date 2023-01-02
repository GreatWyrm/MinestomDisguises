package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.TeamsPacket;

import java.util.List;

public final class TeamTranslation {
    public static void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if(event.getPacket() instanceof TeamsPacket packet) {
            if (packet.action() instanceof TeamsPacket.AddEntitiesToTeamAction action) {
                Player player = event.getPlayer();
                Disguise disguise = parentManager.getPlayerDisguise(player);
                if (disguise != null) {
                    List<String> entities = action.entities().stream().map(entity -> {
                        if (entity.equals(player.getUsername()))
                            return disguise.getUuid().toString();
                        return entity;
                    }).toList();
                    TeamsPacket.AddEntitiesToTeamAction newAction = new TeamsPacket.AddEntitiesToTeamAction(entities);
                    TeamsPacket newPacket = new TeamsPacket(packet.teamName(), newAction);
                    disguise.sendPacketToViewers(newPacket);
                }
            }
        }
    }
}
