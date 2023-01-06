package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityPotionAddEvent;
import net.minestom.server.event.entity.EntityPotionRemoveEvent;
import net.minestom.server.event.trait.EntityEvent;

public final class EffectTranslation {
    public static void listener(EntityEvent event, DisguiseManager parentManager) {
        if(!(event.getEntity() instanceof Player player)) return;
        if (event instanceof EntityPotionAddEvent e) {
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null)
                disguise.addEffect(e.getPotion());
        }
        if (event instanceof EntityPotionRemoveEvent e) {
            Disguise disguise = parentManager.getPlayerDisguise(player);
            if (disguise != null)
                disguise.removeEffect(e.getPotion().effect());
        }
    }
}
