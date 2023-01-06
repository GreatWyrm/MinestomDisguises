package com.arcanewarrior.disguises.commands;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShowCommand extends Command {

    public ShowCommand(DisguiseManager disguiseManager) {
        super("show");

        ArgumentEntity playerArg = ArgumentType.Entity("players").onlyPlayers(true);
        playerArg.setSuggestionCallback((sender, context, suggestion) -> {
            for(String string : disguiseManager.getHiddenUsernames()) {
                suggestion.addEntry(new SuggestionEntry(string));
            }
        });

        addSyntax((sender, context) -> {
            List<Entity> playerList = context.get(playerArg).find(sender);
            List<Player> toShow = new ArrayList<>();
            for(Entity entity : playerList) {
                if(entity instanceof Player player) {
                    toShow.add(player);
                }
            }
            disguiseManager.showPlayers(toShow);
        }, playerArg);
    }
}
