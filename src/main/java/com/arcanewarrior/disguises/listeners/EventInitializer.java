package com.arcanewarrior.disguises.listeners;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import com.arcanewarrior.disguises.MinestomDisguises;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.tag.Tag;

public record EventInitializer(EventNode<Event> node, Tag<Boolean> hideTag) {

    private static final String nodeName = "disguises-player-events";

    public void registerAll(DisguiseManager disguiseManager) {
        // Entity Events
        EventNode<EntityEvent> entityParent = EventNode.type(nodeName, EventFilter.ENTITY);
        // Automatically make players hide
        entityParent.addListener(PlayerLoginEvent.class, event -> event.getPlayer().updateViewerRule(entity -> !entity.hasTag(hideTag)));
        // Remove Disguise when Player Leave
        entityParent.addListener(PlayerDisconnectEvent.class, this::disconnectListener);
        // Register Disguise Events
        DisguiseEvents events = new DisguiseEvents(disguiseManager);
        events.registerAll(entityParent);

        node.addChild(entityParent);
    }

    public void unregisterAll() {
        node.removeChildren(nodeName);
    }

    private void disconnectListener(PlayerDisconnectEvent event) {
        Player player = event.getPlayer();
        Disguise disguise = MinestomDisguises.getInstance().getDisguiseManager().getPlayerDisguise(player);
        if(disguise != null)
            MinestomDisguises.getInstance().getDisguiseManager().undisguisePlayer(player);
    }
}
