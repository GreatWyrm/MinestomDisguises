package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.utils.PacketUtils;

public final class TeleportTranslation implements DisguiseTranslation<PlayerPacketOutEvent> {
    @Override
    public void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof EntityTeleportPacket packet) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if (disguise != null) {
                EntityTeleportPacket newPacket = new EntityTeleportPacket(disguise.getEntityId(), packet.position(), packet.onGround());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
    }
}
