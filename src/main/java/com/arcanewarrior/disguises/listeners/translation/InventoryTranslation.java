package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;

public final class InventoryTranslation {
    public static void listener(PlayerChangeHeldSlotEvent event, DisguiseManager parentManager) {
        Player player = event.getPlayer();
        Disguise disguise = parentManager.getPlayerDisguise(player);
        if (disguise != null)
            disguise.setItemInMainHand(player.getItemInMainHand());
    }
}
