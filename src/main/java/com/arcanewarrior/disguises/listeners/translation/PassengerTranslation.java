package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.SetPassengersPacket;

import java.util.ArrayList;
import java.util.List;

public final class PassengerTranslation {
    public static void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof SetPassengersPacket packet) {
            Player player = event.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null) {
                int vehicleId = packet.vehicleEntityId();
                if(vehicleId == player.getEntityId())
                    vehicleId = disguise.getEntityId();
                List<Integer> passengers = new ArrayList<>(packet.passengersId().stream().map(id -> {
                    if(id == player.getEntityId())
                        return disguise.getEntityId();
                    return id;
                }).toList());
                SetPassengersPacket newPacket = new SetPassengersPacket(vehicleId, passengers);
                disguise.sendPacketToViewers(newPacket);
            }
        }
    }
}
