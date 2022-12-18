package com.arcanewarrior.disguises;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DisguiseManager {

    private final Tag<Boolean> hideTag;
    // Map from Player to disguises
    private final Map<Player, Entity> disguisedPlayers = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(DisguiseManager.class);
    private final TeamManager teamManager = new TeamManager();
    private final Team disguiseTeam = teamManager.createBuilder("disguises").collisionRule(TeamsPacket.CollisionRule.NEVER).build();

    public DisguiseManager(Tag<Boolean> hideTag) {
        this.hideTag = hideTag;
    }


    // ---------------- DISGUISING PLAYERS --------------

    public void disguisePlayer(Player player, EntityType type) {
        disguisePlayer(player, new Entity(type));
    }
    public void disguisePlayer(Player player, Entity disguise) {
        if(player.getInstance() == null) {
            logger.warn("Failed to disguise " + player.getUsername() + ", as they are in a null instance!");
            return;
        }
        hidePlayer(player);
        logger.info("Disguising " + player.getUsername() + " as a " + disguise.getEntityType().name());
        if(disguise instanceof LivingEntity livingEntity) {
            livingEntity.setTeam(disguiseTeam);
        }
        if(player.getInstance() != disguise.getInstance()) {
            disguise.setInstance(player.getInstance(), player.getPosition());
        } else {
            disguise.teleport(player.getPosition());
        }
        disguisedPlayers.put(player, disguise);
    }

    public void undisguisePlayer(@NotNull Player player) {
        if (!disguisedPlayers.containsKey(player)) {
            logger.warn("Tried to remove the disguise from " + player.getUsername() + ", but they weren't disguised!");
            return;
        }

        disguisedPlayers.get(player).remove();
        disguisedPlayers.remove(player);
        showPlayer(player);
    }

    public boolean isPlayerDisguised(@NotNull Player player) {
        return disguisedPlayers.containsKey(player);
    }

    public @Nullable Entity getPlayerDisguise(@NotNull Player player) {
        return disguisedPlayers.getOrDefault(player, null);
    }


    // ----------- SHOW/HIDE PLAYERS -----------------

    public void hidePlayer(Player player) {
        hidePlayers(List.of(player));
    }

    public void hidePlayers(Collection<Player> players) {
        ArrayList<Player> toUpdate = new ArrayList<>();
        for(Player player : players) {
            if(!player.hasTag(hideTag)) {
                player.setTag(hideTag, true);
                toUpdate.add(player);
            }
        }
        for(Player hidden : toUpdate) {
            for(Player player : hidden.getViewers()) {
                hidden.removeViewer(player);
            }
        }
    }

    public void showPlayer(Player player) {
        showPlayers(List.of(player));
    }
    public void showPlayers(Collection<Player> players) {
        ArrayList<Player> toUpdate = new ArrayList<>();
        for(Player player : players) {
            if(player.hasTag(hideTag)) {
                player.removeTag(hideTag);
                toUpdate.add(player);
            }
        }
        for(Player hidden : toUpdate) {
            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                hidden.addViewer(player);
            }
        }
    }

    public Set<String> getHiddenUsernames() {
        Set<String> usernames = new HashSet<>();
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if(player.hasTag(hideTag)) {
                usernames.add(player.getUsername());
            }
        }
        return usernames;
    }

    public Set<String> getNonHiddenUsernames() {
        Set<String> usernames = new HashSet<>();
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if(!player.hasTag(hideTag)) {
                usernames.add(player.getUsername());
            }
        }
        return usernames;
    }

    // ---- EVENT TRIGGERS -----
}
