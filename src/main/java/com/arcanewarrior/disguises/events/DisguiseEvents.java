package com.arcanewarrior.disguises.events;

import com.arcanewarrior.disguises.Disguise;
import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.utils.PacketUtils;
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
        playerParent.addListener(PlayerPacketEvent.class, this::handlePlayerPacketInput);
        playerParent.addListener(PlayerPacketOutEvent.class, this::playerEntityAnimation);
        playerParent.addListener(PlayerPacketOutEvent.class, this::playerTeleport);
        playerParent.addListener(PlayerPacketOutEvent.class, this::playerSpawn);
        node.addChild(playerParent);
    }


    private void handlePlayerPacketInput(PlayerPacketEvent event) {
        // It would be neat if we could do a switch on event.getPacket(), and then handle the individual cases, but that's in Java 17 preview :(
        if(event.getPacket() instanceof ClientPlayerPositionPacket positionPacket) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
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
        } else if(event.getPacket() instanceof ClientPlayerRotationPacket rotationPacket) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if(disguise != null) {
                // Translate player head move to disguise head move
                EntityRotationPacket disguisePositionPacket = new EntityRotationPacket(disguise.getEntityId(), rotationPacket.yaw(), rotationPacket.pitch(), rotationPacket.onGround());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), disguisePositionPacket);
            }
        } else if(event.getPacket() instanceof ClientInteractEntityPacket interactEntityPacket) {
            // Inversion! We want to see if a player interacted with a disguise and translate internally from there
            Player trueTarget = parentManager.getPlayerFromDisguise(interactEntityPacket.targetId());
            if(trueTarget != null) {
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

    private void playerEntityAnimation(PlayerPacketOutEvent event) {
        if(event.getPacket() instanceof EntityAnimationPacket packet) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if(disguise != null) {
                // Translate animation from player to disguise
                // TODO: Check to see which animations may not need translation, may have to check per entity type
                EntityAnimationPacket newPacket = new EntityAnimationPacket(disguise.getEntityId(), packet.animation());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
    }

    private void playerTeleport(PlayerPacketOutEvent event) {
        if(event.getPacket() instanceof EntityTeleportPacket packet) {
            Disguise disguise = parentManager.getPlayerDisguise(event.getPlayer());
            if(disguise != null) {
                EntityTeleportPacket newPacket = new EntityTeleportPacket(disguise.getEntityId(), packet.position(), packet.onGround());
                PacketUtils.sendGroupedPacket(disguise.getViewers(), newPacket);
            }
        }
    }

    private void playerSpawn(PlayerPacketOutEvent event) {
        if(event.getPacket() instanceof SpawnPlayerPacket packet) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(packet.playerUuid());
            if(player != null) {
                Disguise disguise = parentManager.getPlayerDisguise(player);
                if(disguise != null) {
                    player.removeViewer(event.getPlayer());
                    disguise.addViewer(event.getPlayer());
                }
            }
        }
    }
}
