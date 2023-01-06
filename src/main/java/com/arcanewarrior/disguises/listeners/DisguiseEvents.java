package com.arcanewarrior.disguises.listeners;

import com.arcanewarrior.disguises.DisguiseManager;
import com.arcanewarrior.disguises.listeners.translation.*;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityPotionAddEvent;
import net.minestom.server.event.entity.EntityPotionRemoveEvent;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.*;

public final class DisguiseEvents {
    private final DisguiseManager parentManager;

    public DisguiseEvents(DisguiseManager manager) {
        this.parentManager = manager;
    }

    public void registerAll(EventNode<EntityEvent> node) {
        // Player Events
        EventNode<EntityEvent> playerParent = EventNode.type("disguise-player-events", EventFilter.ENTITY);
        playerParent.addChild(animationTranslations());
        playerParent.addChild(inventoryTranslations());
        playerParent.addChild(effectsTranslations());
        playerParent.addChild(miscTranslations());
        node.addChild(playerParent);
    }

    private EventNode<PlayerEvent> animationTranslations() {
        EventNode<PlayerEvent> node = EventNode.type("animations", EventFilter.PLAYER);
        node.addListener(PlayerItemAnimationEvent.class, event -> AnimationTranslation.listener(event, parentManager));
        node.addListener(PlayerHandAnimationEvent.class, event -> AnimationTranslation.listener(event, parentManager));
        return node;
    }

    private EventNode<PlayerEvent> inventoryTranslations() {
        EventNode<PlayerEvent> node = EventNode.type("inventory", EventFilter.PLAYER);
        node.addListener(PlayerChangeHeldSlotEvent.class, event -> InventoryTranslation.listener(event, parentManager));
        node.addListener(PlayerSwapItemEvent.class, event -> InventoryTranslation.listener(event, parentManager));
        node.addListener(PlayerInventoryItemChangeEvent.class, event -> InventoryTranslation.listener(event, parentManager));
        return node;
    }

    private EventNode<EntityEvent> effectsTranslations() {
        EventNode<EntityEvent> node = EventNode.type("effects", EventFilter.ENTITY);
        node.addListener(EntityPotionAddEvent.class, event -> EffectTranslation.listener(event, parentManager));
        node.addListener(EntityPotionRemoveEvent.class, event -> EffectTranslation.listener(event, parentManager));
        return node;
    }

    @SuppressWarnings("UnstableApiUsage")
    private EventNode<EntityEvent> miscTranslations() {
        EventNode<EntityEvent> node = EventNode.type("misc", EventFilter.ENTITY);
        node.addListener(PlayerPacketEvent.class, this::delegatePacket);
        node.addListener(PlayerPacketOutEvent.class, this::delegatePacket);
        node.addListener(PlayerMoveEvent.class, event -> MoveTranslation.listener(event, parentManager));
        return node;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void delegatePacket(PlayerPacketOutEvent event) {
        ServerPacket packet = event.getPacket();
        if (packet instanceof SetPassengersPacket)
            PassengerTranslation.listener(event, parentManager);
        else if (packet instanceof PlayerPacketOutEvent && parentManager.getConfig().getTable("disguises").getBoolean("translate-teams"))
            TeamTranslation.listener(event, parentManager);
    }

    private void delegatePacket(PlayerPacketEvent event) {
        ClientPacket packet = event.getPacket();
        if(packet instanceof ClientInteractEntityPacket)
            InteractTranslation.listener(event, parentManager);
    }
}
