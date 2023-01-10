package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;

public final class AnimationTranslation {
    public static void listener(PlayerEvent event, DisguiseManager parentManager) {
        if (event instanceof PlayerHandAnimationEvent e) {
            Disguise disguise = parentManager.getPlayerDisguise(e.getPlayer());
            if (disguise != null) {
                switch (e.getHand()) {
                    case OFF -> disguise.swingOffHand();
                    case MAIN -> disguise.swingMainHand();
                }
            }
        } else if (event instanceof PlayerItemAnimationEvent e) {
            Disguise disguise = parentManager.getPlayerDisguise(e.getPlayer());
            if(disguise != null) {
                // Translate animation from player to disguise
                // TODO: Check to see which animations may not need translation, may have to check per entity type
                if(!e.isCancelled()) {
                    disguise.refreshActiveHand(true, e.getHand() == Player.Hand.OFF, false);
                    disguise.sendPacketToViewers(disguise.getMetadataPacket());
                }
            }
        }
    }

    public static void entityAnimationListener(EntityAnimationPacket packet, DisguiseManager parentManager) {
        Disguise disguise = parentManager.getDisguiseFromEntityId(packet.entityId());
        if (disguise != null) {
            // todo: almost certainly need additional translations, based on exactly which mob you are disguised as
            if (packet.animation() == EntityAnimationPacket.Animation.TAKE_DAMAGE) {
                EntityAnimationPacket newPacket = new EntityAnimationPacket(disguise.getEntityId(), EntityAnimationPacket.Animation.TAKE_DAMAGE);
                disguise.sendPacketToViewers(newPacket);
            }
        }
    }
}
