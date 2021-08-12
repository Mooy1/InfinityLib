package io.github.mooy1.infinitylib.utils;

import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.github.mooy1.infinitylib.core.AbstractAddon;

/**
 * A class for registering event handlers
 *
 * @author Mooy1
 */
@UtilityClass
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Events implements Listener {

    private static final Listener LISTENER = new Events();

    /**
     * Registers the given listener class
     */
    public static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, AbstractAddon.instance());
    }

    /**
     * Registers the given handler to the given event
     */
    @SuppressWarnings("unchecked")
    public static <T extends Event> void register(Class<T> eventClass, EventPriority priority, boolean ignoreCancelled, Consumer<T> handler) {
        AbstractAddon instance = AbstractAddon.instance();
        Bukkit.getPluginManager().registerEvent(eventClass, LISTENER, priority, (listener, event) -> {
            handler.accept((T) event);
        }, instance, ignoreCancelled);
    }

}