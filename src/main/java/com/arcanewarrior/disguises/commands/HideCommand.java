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

public class HideCommand extends Command {
    public HideCommand(DisguiseManager disguiseManager) {
        super("hide");

        ArgumentEntity playerArg = ArgumentType.Entity("players").onlyPlayers(true);
        playerArg.setSuggestionCallback((sender, context, suggestion) -> {
            for(String string : disguiseManager.getNonHiddenUsernames()) {
                suggestion.addEntry(new SuggestionEntry(string));
            }
        });

        addSyntax((sender, context) -> {
            List<Entity> playerList = context.get(playerArg).find(sender);
            List<Player> toHide = new ArrayList<>();
            for(Entity entity : playerList) {
                if(entity instanceof Player player) {
                   toHide.add(player);
                }
            }
            disguiseManager.hidePlayers(toHide);
        }, playerArg);
    }
}
