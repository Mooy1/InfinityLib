package io.github.mooy1.infinitylib;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.logging.Level;

public final class PluginUtils {
    
    private static JavaPlugin plugin = null;
    
    public static final int TICKER_DELAY = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    public static final float TICK_RATIO = 20F / PluginUtils.TICKER_DELAY;

    /**
     * sets up config and utility plugin
     */
    public static void setup(@Nonnull JavaPlugin javaPlugin, @Nonnull String url, @Nonnull File file) {
        validate();
        plugin = javaPlugin;
        javaPlugin.saveDefaultConfig();
        javaPlugin.getConfig().options().copyDefaults(true).copyHeader(true);
        javaPlugin.saveConfig();
        if (javaPlugin.getDescription().getVersion().startsWith("DEV - ")) {
            PluginUtils.log(Level.INFO, "Starting auto update");
            new GitHubBuildsUpdater(plugin, file, url).start();
        } else {
            PluginUtils.log(Level.WARNING, "You must be on a DEV build to auto update!");
        }
    }
    
    public static NamespacedKey getKey(@Nonnull String key) {
        return new NamespacedKey(plugin, key);
    }

    public static void log(@Nonnull Level level , @Nonnull String... logs) {
        validate();
        for (String log : logs) {
            plugin.getLogger().log(level, log);
        }
    }
    
    public static void log(@Nonnull String... logs) {
        validate();
        for (String log : logs) {
            plugin.getLogger().log(Level.INFO, log);
        }
    }
    
    public static void runSync(@Nonnull Runnable runnable, long delay) {
        validate();
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static void scheduleRepeatingSync(@Nonnull Runnable runnable, long delay, long interval) {
        validate();
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, interval);
    }

    public static void runSync(@Nonnull Runnable runnable) {
        validate();
        Validate.notNull(runnable, "Cannot run null");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    private static void validate() {
        Validate.notNull(plugin, "Make sure to set the plugin instance");
    }
    
}
