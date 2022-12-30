package com.arcanewarrior.disguises.commands;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;

import java.util.List;

public class DisguiseCommand extends Command {
    public DisguiseCommand(DisguiseManager manager) {
        super("disguise");

        ArgumentEntity players = ArgumentType.Entity("players").onlyPlayers(true);
        ArgumentEntityType disguiseType = ArgumentType.EntityType("disguise");
        ArgumentWord removeArg = ArgumentType.Word("remove").from("remove");

        addSyntax((sender, context) -> {
            List<Entity> playerList = context.get(players).find(sender);
            EntityType type = context.get(disguiseType);
            for(Entity entity : playerList) {
                if(entity instanceof Player player) {
                    manager.disguisePlayer(player, type);
                }
            }
        }, players, disguiseType);

        addSyntax((sender, context) -> {
            List<Entity> playerList = context.get(players).find(sender);
            for(Entity entity : playerList) {
                if(entity instanceof Player player) {
                    manager.unDisguisePlayer(player);
                }
            }
        }, removeArg, players);
    }
}
