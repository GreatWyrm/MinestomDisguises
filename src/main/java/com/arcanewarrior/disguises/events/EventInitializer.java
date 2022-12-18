package com.arcanewarrior.disguises.events;


import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.tag.Tag;

public class EventInitializer {

    public void registerAll(EventNode<Event> node, Tag<Boolean> hideTag) {
        // Player Events
        EventNode<PlayerEvent> playerParent = EventNode.type("disguises-player-events", EventFilter.PLAYER);
        // Automatically make players hide
        playerParent.addListener(PlayerLoginEvent.class, event -> event.getPlayer().updateViewerRule(entity -> !entity.hasTag(hideTag)));

        node.addChild(playerParent);
    }
}
