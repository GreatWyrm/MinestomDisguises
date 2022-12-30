package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.minestom.server.network.packet.server.play.EntityPositionPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.minestom.server.utils.PacketUtils;

public final class InputTranslation implements DisguiseTranslation<PlayerPacketEvent> {
    @Override
    public void listener(PlayerPacketEvent event, DisguiseManager parentManager) {
        // It would be neat if we could do a switch on event.getPacket(), and then handle the individual cases, but that's in Java 17 preview :(
        if (event.getPacket() instanceof ClientPlayerPositionPacket positionPacket) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if (disguise != null) {
                // Translate player move to disguise move
                // Calculate delta
                // Why this particular calculation? Ask https://wiki.vg/Protocol#Update_Entity_Position
                short xDiff = (short) ((positionPacket.position().x() * 32 - event.getPlayer().getPosition().x() * 32) * 128);
                short yDiff = (short) ((positionPacket.position().y() * 32 - event.getPlayer().getPosition().y() * 32) * 128);
                short zDiff = (short) ((positionPacket.position().z() * 32 - event.getPlayer().getPosition().z() * 32) * 128);
                // Prepare & send packet
                EntityPositionPacket disguisePositionPacket = new EntityPositionPacket(disguise.getEntityId(), xDiff, yDiff, zDiff, positionPacket.onGround());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), disguisePositionPacket);
            }
        } else if (event.getPacket() instanceof ClientPlayerRotationPacket rotationPacket) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if (disguise != null) {
                // Translate player head move to disguise head move
                EntityRotationPacket disguisePositionPacket = new EntityRotationPacket(disguise.getEntityId(), rotationPacket.yaw(), rotationPacket.pitch(), rotationPacket.onGround());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), disguisePositionPacket);
            }
        } else if (event.getPacket() instanceof ClientInteractEntityPacket interactEntityPacket) {
            // Inversion! We want to see if a player interacted with a disguise and translate internally from there
            Player trueTarget = parentManager.getPlayerFromDisguise(interactEntityPacket.targetId());
            if (trueTarget != null) {
                // Interaction safety checks
                if (event.getPlayer().getDistance(trueTarget) < 6 && !trueTarget.isDead()) {
                    ClientInteractEntityPacket.Type type = interactEntityPacket.type();
                    if (type instanceof ClientInteractEntityPacket.Attack) {
                        EventDispatcher.call(new EntityAttackEvent(event.getPlayer(), trueTarget));
                    } else if (type instanceof ClientInteractEntityPacket.InteractAt interactAt) {
                        EventDispatcher.call(new PlayerEntityInteractEvent(event.getPlayer(), trueTarget, interactAt.hand()));
                    }
                }
                // Cancel the event so the original input does not go through
                event.setCancelled(true);
            }

        }
    }
}
