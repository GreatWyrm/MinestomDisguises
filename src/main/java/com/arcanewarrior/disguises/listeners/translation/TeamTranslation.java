package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;

import java.util.List;

public final class TeamTranslation {
    public static void listener(Player player, TeamsPacket teamsPacket, DisguiseManager parentManager) {
        if (teamsPacket.action() instanceof TeamsPacket.AddEntitiesToTeamAction action) {
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null) {
                List<String> entities = action.entities().stream().map(entity -> {
                    if (entity.equals(player.getUsername()))
                        return disguise.getUuid().toString();
                    return entity;
                }).toList();
                TeamsPacket.AddEntitiesToTeamAction newAction = new TeamsPacket.AddEntitiesToTeamAction(entities);
                TeamsPacket newPacket = new TeamsPacket(teamsPacket.teamName(), newAction);
                disguise.sendPacketToViewers(newPacket);
            }
        }
    }
}
