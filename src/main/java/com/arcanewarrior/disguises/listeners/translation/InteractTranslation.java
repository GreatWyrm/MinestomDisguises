package com.arcanewarrior.disguises.listeners.translation;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;

public final class InteractTranslation {
    public static void listener(PlayerPacketEvent event, DisguiseManager parentManager) {
        if (event.getPacket() instanceof ClientInteractEntityPacket interactEntityPacket) {
            // Inversion! We want to see if a player interacted with a disguise and translate internally from there
            Player trueTarget = parentManager.getPlayerFromDisguise(interactEntityPacket.targetId());
            if (trueTarget != null) {
                // Interaction safety checks
                if (event.getPlayer().getDistance(trueTarget) < 6 && !trueTarget.isDead()) {
                    ClientInteractEntityPacket.Type type = interactEntityPacket.type();
                    if (type instanceof ClientInteractEntityPacket.Attack) {
                        EventDispatcher.call(new EntityAttackEvent(event.getPlayer(), trueTarget));
                    } else if (type instanceof ClientInteractEntityPacket.InteractAt interactAt) {
                        Point interactPosition = new Vec(interactAt.targetX(), interactAt.targetY(), interactAt.targetZ());
                        EventDispatcher.call(new PlayerEntityInteractEvent(event.getPlayer(), trueTarget, interactAt.hand(), interactPosition));
                    }
                }
                // Cancel the event so the original input does not go through
                event.setCancelled(true);
            }

        }
    }
}
