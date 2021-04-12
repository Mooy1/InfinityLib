package io.github.mooy1.infinitylib;

import java.util.logging.Level;

import lombok.experimental.UtilityClass;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import io.github.mooy1.infinitylib.commands.AbstractCommand;

/**
 * A static interface to an {@link AbstractAddon}
 */
@UtilityClass
public final class PluginUtils {
    
    private static AbstractAddon PLUGIN;
    
    public static void setPlugin(AbstractAddon addon) {
        if (PLUGIN != null) {
            throw new IllegalStateException("Plugin is already set!");
        } else {
            PLUGIN = addon;
        }
    }

    public static AbstractAddon getPlugin() {
        if (PLUGIN == null) {
            throw new IllegalStateException("Plugin was not set!");
        } else {
            return PLUGIN;
        }
    }

    public static void log(String... messages) {
        getPlugin().log(messages);
    }

    public static void log(Level level, String... messages) {
        getPlugin().log(level, messages);
    }

    public static void registerListener(Listener... listeners) {
        getPlugin().registerListener(listeners);
    }

    public static void runSync(Runnable runnable) {
        getPlugin().runSync(runnable);
    }

    public static void runSync(Runnable runnable, long delay) {
        getPlugin().runSync(runnable, delay);
    }

    public static void scheduleRepeatingSync(Runnable runnable, long interval) {
        getPlugin().scheduleRepeatingSync(runnable, interval);
    }

    public static void scheduleRepeatingSync(Runnable runnable, long delay, long interval) {
        getPlugin().scheduleRepeatingSync(runnable, delay, interval);
    }

    public static void runAsync(Runnable runnable) {
        getPlugin().runAsync(runnable);
    }

    public static void runAsync(Runnable runnable, long delay) {
        getPlugin().runAsync(runnable, delay);
    }

    public static void scheduleRepeatingAsync(Runnable runnable, long interval) {
        getPlugin().scheduleRepeatingAsync(runnable, interval);
    }

    public static void scheduleRepeatingAsync(Runnable runnable, long delay, long interval) {
        getPlugin().scheduleRepeatingAsync(runnable, delay, interval);
    }

    public static void addSubCommands(String command, AbstractCommand... commands) {
        getPlugin().addSubCommands(command, commands);
    }

    public static NamespacedKey getKey(String s) {
        return getPlugin().getKey(s);
    }

    public static AddonConfig loadConfig(String name) {
        return getPlugin().loadConfig(name);
    }
    
    public static int getGlobalTick() {
        return getPlugin().getGlobalTick();
    }

}
