package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.EntityEffectPacket;
import net.minestom.server.network.packet.server.play.RemoveEntityEffectPacket;
import net.minestom.server.utils.PacketUtils;

public final class EffectTranslation {
    public static void listener(PlayerPacketOutEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof EntityEffectPacket packet) {
            Player player = event.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null) {
                EntityEffectPacket newPacket = new EntityEffectPacket(disguise.getEntityId(), packet.potion(), packet.factorCodec());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
        if (event.getPacket() instanceof RemoveEntityEffectPacket packet) {
            Player player = event.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null) {
                RemoveEntityEffectPacket newPacket = new RemoveEntityEffectPacket(disguise.getEntityId(), packet.potionEffect());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
    }
}
