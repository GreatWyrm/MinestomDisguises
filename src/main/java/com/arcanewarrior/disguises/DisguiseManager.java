package com.arcanewarrior.disguises;

import com.arcanewarrior.disguises.events.PlayerDisguiseEvent;
import com.arcanewarrior.disguises.events.PlayerUndisguiseEvent;
import com.moandjiezana.toml.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class DisguiseManager {

    private final Toml config;
    private final Tag<Boolean> hideTag;
    private final Map<Player, Disguise> disguisedPlayers = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(DisguiseManager.class);
    private final TeamManager teamManager = new TeamManager();
    private final Team disguiseTeam = teamManager.createBuilder("disguises").collisionRule(TeamsPacket.CollisionRule.NEVER).build();

    public DisguiseManager(Tag<Boolean> hideTag, Toml config) {
        this.hideTag = hideTag;
        this.config = config;
    }


    // ---------------- DISGUISING PLAYERS --------------

    public void disguisePlayer(Player player, EntityType type) {
        disguisePlayer(player, new Disguise(type, player, this));
    }
    public void disguisePlayer(Player player, Disguise disguise) {
        if(player.getInstance() == null) {
            logger.warn("Failed to disguise " + player.getUsername() + ", as they are in a null instance!");
            return;
        }
        PlayerDisguiseEvent event = new PlayerDisguiseEvent(player, disguise);
        MinecraftServer.getGlobalEventHandler().call(event);
        if(event.isCancelled()) return;
        hidePlayer(player);
        logger.info("Disguising " + player.getUsername() + " as a " + disguise.getEntityType().name());
        if(shouldTranslateTeams())
            disguise.setTeam(disguiseTeam);
        else
            disguise.setTeam(player.getTeam());
        disguise.setAutoViewable(false);
        if(player.getInstance() != disguise.getInstance())
            disguise.setInstance(player.getInstance(), player.getPosition());
        else
            disguise.teleport(player.getPosition());
        disguisedPlayers.put(player, disguise);
        player.getViewers().forEach(disguise::addViewer);
    }

    public void undisguisePlayer(@NotNull Player player) {
        if (!disguisedPlayers.containsKey(player)) {
            logger.warn("Tried to remove the disguise from " + player.getUsername() + ", but they weren't disguised!");
            return;
        }
        Disguise disguise = disguisedPlayers.get(player);
        PlayerUndisguiseEvent event = new PlayerUndisguiseEvent(player, disguise);
        if(event.isCancelled()) return;
        disguise.remove();
        disguisedPlayers.remove(player, disguise);
        showPlayer(player);
    }

    public boolean isPlayerDisguised(@NotNull Player player) {
        return disguisedPlayers.containsKey(player);
    }

    public @Nullable Disguise getPlayerDisguise(@NotNull Player player) {
        return disguisedPlayers.getOrDefault(player, null);
    }

    /**
     * Gets the player that is wearing the disguise based on the entity ID
     * @param entityId The entity id
     * @return The player object that has the disguise, or null if it's a normal entity
     */
    public @Nullable Player getPlayerFromDisguise(int entityId) {
        for (var entry : disguisedPlayers.entrySet()) {
            if(entry.getValue().getEntityId() == entityId) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Given a player's entity id, gets the disguise that they have
     * @param entityId the entity id of a player
     * @return the disguise object they have, or null if they do not have one
     */
    public @Nullable Disguise getDisguiseFromEntityId(int entityId) {
        for (var entry : disguisedPlayers.entrySet()) {
            if(entry.getKey().getEntityId() == entityId) {
                return entry.getValue();
            }
        }
        return null;
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
        for(Player hidden : toUpdate)
            for(Player player : hidden.getViewers()) {
                hidden.removeViewer(player);
                player.sendPacket(new DestroyEntitiesPacket(hidden.getEntityId()));
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
        for(Player hidden : toUpdate)
            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
                hidden.addViewer(player);
    }

    public Set<String> getHiddenUsernames() {
        Set<String> usernames = new HashSet<>();
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
            if(player.hasTag(hideTag))
                usernames.add(player.getUsername());
        return usernames;
    }

    public Set<String> getNonHiddenUsernames() {
        Set<String> usernames = new HashSet<>();
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
            if(!player.hasTag(hideTag))
                usernames.add(player.getUsername());
        return usernames;
    }

    public Toml getConfig() {
        return config;
    }

    public boolean shouldTranslateTeams() {
         return config.getTable("disguises").getBoolean("translate-teams", false);
    }

    // ---- EVENT TRIGGERS -----
}
