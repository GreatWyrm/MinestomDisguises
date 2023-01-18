package com.arcanewarrior.disguises;

import com.arcanewarrior.disguises.commands.CommandInitializer;
import com.arcanewarrior.disguises.listeners.EventInitializer;
import com.moandjiezana.toml.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.tag.Tag;
import org.hypejet.hypestom.Extension;

public final class MinestomDisguises extends Extension {

    private static MinestomDisguises instance;
    private CommandInitializer commandInitializer;
    private EventInitializer eventInitializer;
    private DisguiseManager disguiseManager;
    private final Tag<Boolean> hideTag = Tag.Boolean("disguises-hidden");

    @Override
    public void initialize() {
        instance = this;

        final Toml config = getConfig();
        if(config == null) return;

        eventInitializer = new EventInitializer(getEventNode(), hideTag);
        disguiseManager = new DisguiseManager(hideTag, config);
        eventInitializer.registerAll(disguiseManager);

        if (!config.getTable("extension").getBoolean("only-api", false)) {
            commandInitializer = new CommandInitializer(disguiseManager);
            commandInitializer.registerAll(this);
        }
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
