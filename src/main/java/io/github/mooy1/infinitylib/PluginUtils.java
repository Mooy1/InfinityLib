package io.github.mooy1.infinitylib;

import io.github.mooy1.infinitylib.commands.AbstractCommand;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import java.util.logging.Level;

/**
 * A static interface to an {@link AbstractAddon}'s methods
 */
public final class PluginUtils {
    
    @Getter
    private static AbstractAddon plugin;
    
    public static void setAddon(AbstractAddon addon) {
        if (plugin != null) {
            throw new IllegalStateException("Plugin is already set!");
        } else {
            plugin = addon;
        }
    }
    
    private static void validate() {
        Validate.notNull(plugin, "Plugin was not set!");
    }

    public static void log(String... messages) {
        validate();
        plugin.log(messages);
    }

    public static void log(Level level, String... messages) {
        validate();
        plugin.log(level, messages);
    }

    public static void registerListener(Listener... listeners) {
        validate();
        plugin.registerListener(listeners);
    }

    public static void runSync(Runnable runnable) {
        validate();
        plugin.runSync(runnable);
    }

    public static void runSync(Runnable runnable, long delay) {
        validate();
        plugin.runSync(runnable, delay);
    }

    public static void scheduleRepeatingSync(Runnable runnable, long interval) {
        validate();
        plugin.scheduleRepeatingSync(runnable, interval);
    }

    public static void scheduleRepeatingSync(Runnable runnable, long delay, long interval) {
        validate();
        plugin.scheduleRepeatingSync(runnable, delay, interval);
    }

    public static void runAsync(Runnable runnable) {
        validate();
        plugin.runAsync(runnable);
    }

    public static void runAsync(Runnable runnable, long delay) {
        validate();
        plugin.runAsync(runnable, delay);
    }

    public static void scheduleRepeatingAsync(Runnable runnable, long interval) {
        validate();
        plugin.scheduleRepeatingAsync(runnable, interval);
    }

    public static void scheduleRepeatingAsync(Runnable runnable, long delay, long interval) {
        validate();
        plugin.scheduleRepeatingAsync(runnable, delay, interval);
    }

    public static void addSubCommands(String command, AbstractCommand... commands) {
        validate();
        plugin.addSubCommands(command, commands);
    }

    public static NamespacedKey getKey(String s) {
        validate();
        return plugin.getKey(s);
    }

    public static Config loadConfig(String name) {
        validate();
        return plugin.loadConfig(name);
    }

    public static Config loadConfigWithDefaults(String name) {
        validate();
        return plugin.loadConfigWithDefaults(name);
    }

    public static Config attachConfigDefaults(Config config, String resource) {
        validate();
        return plugin.attachConfigDefaults(config, resource);
    }

    public static int getConfigInt(String path, int min, int max) {
        validate();
        return plugin.getConfigInt(path, min, max);
    }

    public static double getConfigDouble(String path, double min, double max) {
        validate();
        return plugin.getConfigDouble(path, min, max);
    }
    
    public static int getGlobalTick() {
        validate();
        return plugin.getGlobalTick();
    }

}
