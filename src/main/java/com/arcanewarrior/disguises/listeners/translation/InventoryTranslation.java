package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;

public final class InventoryTranslation {
    public static void equipmentChangeListener(PlayerInventoryItemChangeEvent event, DisguiseManager parentManager) {
        Player player = event.getPlayer();
        Disguise disguise = parentManager.getPlayerDisguise(player);
        if(disguise == null) return;

        int slotIndex = event.getSlot();
        if (slotIndex == player.getHeldSlot()) {
            disguise.setEquipment(EquipmentSlot.MAIN_HAND, event.getNewItem());
        } else if(slotIndex == PlayerInventoryUtils.OFFHAND_SLOT) {
            disguise.setEquipment(EquipmentSlot.OFF_HAND, event.getNewItem());
        } else if (slotIndex == PlayerInventoryUtils.BOOTS_SLOT) {
            disguise.setEquipment(EquipmentSlot.BOOTS, event.getNewItem());
        } else if (slotIndex == PlayerInventoryUtils.LEGGINGS_SLOT) {
            disguise.setEquipment(EquipmentSlot.LEGGINGS, event.getNewItem());
        } else if (slotIndex == PlayerInventoryUtils.CHESTPLATE_SLOT) {
            disguise.setEquipment(EquipmentSlot.CHESTPLATE, event.getNewItem());
        } else if (slotIndex == PlayerInventoryUtils.HELMET_SLOT) {
            disguise.setEquipment(EquipmentSlot.HELMET, event.getNewItem());
        }
    }
    public static void changeSlotListener(PlayerChangeHeldSlotEvent event, DisguiseManager parentManager) {
        Player player = event.getPlayer();
        Disguise disguise = parentManager.getPlayerDisguise(player);
        if (disguise != null)
            disguise.setItemInMainHand(player.getInventory().getItemStack(event.getSlot()));
    }

    public static void swapItemListener(PlayerSwapItemEvent event, DisguiseManager parentManager) {
        Player player = event.getPlayer();
        Disguise disguise = parentManager.getPlayerDisguise(player);
        if(disguise != null) {
            disguise.setItemInMainHand(event.getMainHandItem());
            disguise.setItemInOffHand(event.getOffHandItem());
        }
    }
}
