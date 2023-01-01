package com.arcanewarrior.disguises.listeners;

import com.arcanewarrior.disguises.MinestomDisguises;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.tag.Tag;

public record EventInitializer(EventNode<Event> node, Tag<Boolean> hideTag) {

    private static final String nodeName = "disguises-player-events";

    public void registerAll() {
        // Entity Events
        EventNode<EntityEvent> playerParent = EventNode.type(nodeName, EventFilter.ENTITY);
        // Automatically make players hide
        playerParent.addListener(PlayerLoginEvent.class, event -> event.getPlayer().updateViewerRule(entity -> !entity.hasTag(hideTag)));
        // Register Disguise Events
        DisguiseEvents events = new DisguiseEvents(MinestomDisguises.getInstance().getDisguiseManager());
        events.registerAll(playerParent);

        node.addChild(playerParent);
    }

    public void unregisterAll() {
        node.removeChildren(nodeName);
    }
}
