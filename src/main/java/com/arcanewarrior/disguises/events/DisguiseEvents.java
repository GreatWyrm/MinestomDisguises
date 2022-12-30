package com.arcanewarrior.disguises.events;

import com.arcanewarrior.disguises.DisguiseManager;
import com.arcanewarrior.disguises.events.translation.*;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.PlayerEvent;
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
        registerTranslation(PlayerPacketEvent.class, new InputTranslation(), playerParent);
        registerTranslation(PlayerPacketOutEvent.class, new AnimationTranslation(), playerParent);
        registerTranslation(PlayerPacketOutEvent.class, new TeleportTranslation(), playerParent);
        registerTranslation(PlayerPacketOutEvent.class, new SpawnTranslation(), playerParent);
        registerTranslation(PlayerPacketOutEvent.class, new EffectTranslation(), playerParent);
        registerTranslation(PlayerPacketOutEvent.class, new VelocityTranslation(), playerParent);
        registerTranslation(PlayerPacketOutEvent.class, new PassengerTranslation(), playerParent);
        node.addChild(playerParent);
    }

    public <T extends PlayerEvent> void registerTranslation(Class<? extends T> type, DisguiseTranslation<T> translation, EventNode<PlayerEvent> node) {
        node.addListener(type, event -> translation.listener(event, parentManager));
    }
}
