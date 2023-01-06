package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.event.trait.PlayerEvent;

public final class InventoryTranslation {
    public static void listener(PlayerEvent event, DisguiseManager parentManager) {
        if(event instanceof PlayerChangeHeldSlotEvent e) {
            Player player = e.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null)
                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> disguise.setItemInMainHand(player.getItemInMainHand()));
        } else if (event instanceof PlayerInventoryItemChangeEvent e) {
            Player player = e.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if(disguise != null)
                for(EquipmentSlot slot : EquipmentSlot.values())
                    disguise.setEquipment(slot, player.getEquipment(slot));
        } else if(event instanceof PlayerSwapItemEvent e) {
            Player player = e.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if(disguise != null) {
                disguise.setItemInMainHand(e.getMainHandItem());
                disguise.setItemInOffHand(e.getOffHandItem());
            }
        }
    }
}
