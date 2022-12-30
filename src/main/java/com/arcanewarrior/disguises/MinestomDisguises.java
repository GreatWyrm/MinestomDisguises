package com.arcanewarrior.disguises;

import com.arcanewarrior.disguises.commands.CommandInitializer;
import com.arcanewarrior.disguises.events.EventInitializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.tag.Tag;
import xyz.citywide.citystom.Extension;

public final class MinestomDisguises extends Extension {

    private static MinestomDisguises instance;
    private CommandInitializer commandInitializer;
    private EventInitializer eventInitializer;
    private DisguiseManager disguiseManager;

    private final Tag<Boolean> hideTag = Tag.Boolean("disguises-hidden");

    @Override
    public void initialize() {
        instance = this;

        eventInitializer = new EventInitializer(getEventNode(), hideTag);
        disguiseManager = new DisguiseManager(hideTag);
        commandInitializer = new CommandInitializer(disguiseManager);

        commandInitializer.registerAll(this);
        eventInitializer.registerAll();
    }

    @Override
    public void terminate() {
        commandInitializer.unregisterAll(MinecraftServer.getCommandManager());
        eventInitializer.unregisterAll();
    }

    public static MinestomDisguises getInstance() {
        return instance;
    }

    public DisguiseManager getDisguiseManager() {
        return disguiseManager;
    }
}
