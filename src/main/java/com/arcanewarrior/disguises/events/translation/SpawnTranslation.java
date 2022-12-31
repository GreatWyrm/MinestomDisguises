package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.SpawnPlayerPacket;

public final class SpawnTranslation {
    public static void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof SpawnPlayerPacket packet) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(packet.playerUuid());
            if (player != null && !player.equals(event.getPlayer())) {
                Disguise disguise = parentManager.getPlayerDisguise(player);
                if (disguise != null) {
                    player.removeViewer(event.getPlayer());
                    disguise.addViewer(event.getPlayer());
                }
            }
        }
    }
}
