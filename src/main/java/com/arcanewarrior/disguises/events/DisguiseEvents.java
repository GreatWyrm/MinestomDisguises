package com.arcanewarrior.disguises.events;

import com.arcanewarrior.disguises.DisguiseManager;
import com.arcanewarrior.disguises.events.translation.*;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
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

    public void registerAll(EventNode<PlayerEvent> node) {
        // Player Events
        EventNode<PlayerEvent> playerParent = EventNode.type("disguise-player-events", EventFilter.PLAYER);
        playerParent.addListener(PlayerPacketEvent.class, this::delegatePacket);
        playerParent.addListener(PlayerPacketOutEvent.class, this::delegatePacket);
        node.addChild(playerParent);
    }

    public void delegatePacket(PlayerPacketOutEvent event) {
        ServerPacket packet = event.getPacket();
        if(packet instanceof EntityAnimationPacket)
            AnimationTranslation.listener(event, parentManager);
        else if(packet instanceof EntityEffectPacket || packet instanceof RemoveEntityEffectPacket)
            EffectTranslation.listener(event, parentManager);
        else if (packet instanceof SetPassengersPacket)
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
        if(packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerRotationPacket
                || packet instanceof ClientInteractEntityPacket)
            InputTranslation.listener(event, parentManager);
    }
}
