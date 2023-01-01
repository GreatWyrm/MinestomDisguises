package com.arcanewarrior.disguises.commands;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

public class HideCommand extends Command {
    public HideCommand(DisguiseManager disguiseManager) {
        super("hide");

        ArgumentEntity playerArg = ArgumentType.Entity("players").onlyPlayers(true);
        playerArg.setSuggestionCallback((sender, context, suggestion) -> {
            for(String string : disguiseManager.getNonHiddenUsernames()) {
                suggestion.addEntry(new SuggestionEntry(string));
            }
        });
    }
}
