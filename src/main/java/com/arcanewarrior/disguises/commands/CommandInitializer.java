package com.arcanewarrior.disguises.commands;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import xyz.citywide.citystom.Extension;

import java.util.HashSet;

public class CommandInitializer {

    private final HashSet<Command> disguiseCommands = new HashSet<>();

    public CommandInitializer(DisguiseManager disguiseManager) {
        disguiseCommands.add(new HideCommand(disguiseManager));
        disguiseCommands.add(new ShowCommand(disguiseManager));
        disguiseCommands.add(new DisguiseCommand(disguiseManager));
    }

    public void registerAll(Extension extension) {
        for(Command command : disguiseCommands) {
            extension.registerCommand(command);
        }
    }

    public void unregisterAll(CommandManager manager) {
        for(Command command : disguiseCommands) {
            manager.unregister(command);
        }
    }
}
