package io.github.mooy1.infinitylib.common;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class MockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
