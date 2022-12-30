package com.arcanewarrior.disguises.events.translation;

import com.arcanewarrior.disguises.DisguiseManager;
import net.minestom.server.event.trait.PlayerEvent;

public sealed interface DisguiseTranslation<T extends PlayerEvent> permits AnimationTranslation, EffectTranslation, InputTranslation, PassengerTranslation, SpawnTranslation, TeleportTranslation, VelocityTranslation {
    void listener(T event, DisguiseManager parentManager);
}
