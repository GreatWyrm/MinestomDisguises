package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;

public final class InventoryTranslation {
    public static void listener(PlayerEvent event, DisguiseManager parentManager) {
        if(event instanceof PlayerChangeHeldSlotEvent e) {
            Player player = e.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null)
                disguise.setItemInMainHand(player.getInventory().getItemStack(e.getSlot()));
        } else if (event instanceof PlayerInventoryItemChangeEvent e) {
            Player player = e.getPlayer();
            Disguise disguise = parentManager.getPlayerDisguise(player);
            int slotIndex = e.getSlot();
            if(disguise == null) return;

            if (slotIndex == player.getHeldSlot()) {
                disguise.setEquipment(EquipmentSlot.MAIN_HAND, e.getNewItem());
            } else if(slotIndex == PlayerInventoryUtils.OFFHAND_SLOT) {
                disguise.setEquipment(EquipmentSlot.OFF_HAND, e.getNewItem());
            } else if (slotIndex == PlayerInventoryUtils.BOOTS_SLOT) {
                disguise.setEquipment(EquipmentSlot.BOOTS, e.getNewItem());
            } else if (slotIndex == PlayerInventoryUtils.LEGGINGS_SLOT) {
                disguise.setEquipment(EquipmentSlot.LEGGINGS, e.getNewItem());
            } else if (slotIndex == PlayerInventoryUtils.CHESTPLATE_SLOT) {
                disguise.setEquipment(EquipmentSlot.CHESTPLATE, e.getNewItem());
            } else if (slotIndex == PlayerInventoryUtils.HELMET_SLOT) {
                disguise.setEquipment(EquipmentSlot.HELMET, e.getNewItem());
            }
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
