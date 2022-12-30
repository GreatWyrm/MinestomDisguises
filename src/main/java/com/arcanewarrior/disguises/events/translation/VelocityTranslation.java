package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import net.minestom.server.utils.PacketUtils;

public final class VelocityTranslation implements DisguiseTranslation<PlayerPacketOutEvent> {
    @Override
    public void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof EntityVelocityPacket packet) {
            Player player = event.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null) {
                EntityVelocityPacket newPacket = new EntityVelocityPacket(packet.entityId(), packet.velocityX(), packet.velocityY(), packet.velocityZ());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
    }
}
