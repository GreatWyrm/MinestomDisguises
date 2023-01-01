package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.SetPassengersPacket;
import net.minestom.server.utils.PacketUtils;

public final class PassengerTranslation {
    public static void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof SetPassengersPacket packet) {
            Player player = event.getPlayer();
            if (packet.vehicleEntityId() == player.getEntityId()) {
                Disguise disguise = parentManager.getPlayerDisguise(player);
                if (disguise != null) {
                    SetPassengersPacket newPacket = new SetPassengersPacket(disguise.getEntityId(), packet.passengersId());
                    PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
                }
            }
        }
    }
}
