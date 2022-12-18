package com.arcanewarrior.disguises.events;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.minestom.server.network.packet.server.play.EntityPositionPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.minestom.server.utils.PacketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisguiseEvents {
    private final DisguiseManager parentManager;
    private final Logger logger = LoggerFactory.getLogger(DisguiseEvents.class);
    public DisguiseEvents(DisguiseManager manager) {
        this.parentManager = manager;
    }

    public void registerAll(EventNode<Event> node) {
        // Player Events
        EventNode<PlayerEvent> playerParent = EventNode.type("disguises-events", EventFilter.PLAYER);
        playerParent.addListener(PlayerPacketEvent.class, this::onPlayerMove);
        node.addChild(playerParent);
    }


    private void onPlayerMove(PlayerPacketEvent event) {
        if(event.getPacket() instanceof ClientPlayerPositionPacket positionPacket) {
            Entity disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if(disguise != null) {
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
        }

        if(event.getPacket() instanceof ClientPlayerRotationPacket rotationPacket) {
            Entity disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if(disguise != null) {
                // Translate player head move to disguise head move
                EntityRotationPacket disguisePositionPacket = new EntityRotationPacket(disguise.getEntityId(), rotationPacket.yaw(), rotationPacket.pitch(), rotationPacket.onGround());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), disguisePositionPacket);
            }
        }
    }
}
