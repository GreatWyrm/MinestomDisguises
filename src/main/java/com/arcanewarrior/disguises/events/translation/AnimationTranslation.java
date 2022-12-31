package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;
import net.minestom.server.utils.PacketUtils;

public final class AnimationTranslation {
    public static void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof EntityAnimationPacket packet) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if (disguise != null) {
                // Translate animation from player to disguise
                // TODO: Check to see which animations may not need translation, may have to check per entity type
                EntityAnimationPacket newPacket = new EntityAnimationPacket(disguise.getEntityId(), packet.animation());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
    }
}
