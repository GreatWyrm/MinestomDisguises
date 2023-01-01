package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.minestom.server.event.trait.PlayerEvent;

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
                    // TODO: Check hand
                    disguise.refreshActiveHand(true, false, false);
                    disguise.sendPacketToViewers(disguise.getMetadataPacket());
                }
            }
        }
    }
}
