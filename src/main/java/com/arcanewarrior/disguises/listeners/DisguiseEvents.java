package com.arcanewarrior.disguises.listeners;

import com.arcanewarrior.disguises.DisguiseManager;
import com.arcanewarrior.disguises.listeners.translation.*;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DisguiseEvents {
    private final DisguiseManager parentManager;
    private final Logger logger = LoggerFactory.getLogger(DisguiseEvents.class);

    public DisguiseEvents(DisguiseManager manager) {
        this.parentManager = manager;
    }

    public void registerAll(EventNode<EntityEvent> node) {
        // Player Events
        EventNode<EntityEvent> playerParent = EventNode.type("disguise-player-events", EventFilter.ENTITY);
        playerParent.addListener(PlayerPacketEvent.class, this::delegatePacket);
        playerParent.addListener(PlayerPacketOutEvent.class, this::delegatePacket);
        playerParent.addListener(PlayerMoveEvent.class, event -> MoveTranslation.listener(event, parentManager));
        playerParent.addListener(PlayerItemAnimationEvent.class, event -> AnimationTranslation.listener(event, parentManager));
        playerParent.addListener(PlayerHandAnimationEvent.class, event -> AnimationTranslation.listener(event, parentManager));
        playerParent.addListener(PlayerChangeHeldSlotEvent.class, event -> InventoryTranslation.listener(event, parentManager));
        node.addChild(playerParent);
    }

    public void delegatePacket(PlayerPacketOutEvent event) {
        ServerPacket packet = event.getPacket();
        if (packet instanceof SetPassengersPacket)
            PassengerTranslation.listener(event, parentManager);
        else if (packet instanceof SpawnPlayerPacket)
            SpawnTranslation.listener(event, parentManager);
        else if (packet instanceof EntityTeleportPacket)
            TeleportTranslation.listener(event, parentManager);
        else if (packet instanceof EntityVelocityPacket)
            VelocityTranslation.listener(event, parentManager);
    }

    public void delegatePacket(PlayerPacketEvent event) {
        ClientPacket packet = event.getPacket();
        if(packet instanceof ClientInteractEntityPacket)
            InteractTranslation.listener(event, parentManager);
    }
}
