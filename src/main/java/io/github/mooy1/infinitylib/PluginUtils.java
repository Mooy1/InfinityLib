package io.github.mooy1.infinitylib;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

@UtilityClass
public final class PluginUtils {
    
    @Getter
    private static JavaPlugin plugin = null;
    
    @Getter
    private static SlimefunAddon addon = null;
    
    @Getter
    private static int currentTick = 0;
    
    @Getter
    private static long timings = 0;
    
    @Getter
    private static String prefix = null;
    
    public static final int TICKER_DELAY = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    
    public static final float TICK_RATIO = 20F / PluginUtils.TICKER_DELAY;

    /**
     * sets up plugin config and starts auto updater
     */
    public static void setup(@Nonnull String messagePrefix, @Nonnull SlimefunAddon slimefunAddon, @Nonnull String url, @Nonnull File file) {
        addon = slimefunAddon;
        plugin = addon.getJavaPlugin();
        prefix = ChatColor.GRAY + "[" + prefix + ChatColor.GRAY + "] " + ChatColor.WHITE;
        
        // copy config if not present
        plugin.saveDefaultConfig();
        
        // remove unused fields in config
        clearUnused(plugin.getConfig(), plugin.getConfig().getDefaults());
        
        // copy defaults and header to update stuff
        plugin.getConfig().options().copyDefaults(true).copyHeader(true);
        
        // save
        plugin.saveConfig();

        // auto update
        if (plugin.getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(plugin, file, url).start();
        }
    }

    /**
     * Recursively clears unused fields in a config
     */
    private static void clearUnused(ConfigurationSection config, ConfigurationSection defaultConfig) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(defaultConfig);
        for (String key : config.getKeys(false)) {
            if (!defaultConfig.contains(key)) {
                config.set(key, null);
            } else if (config.isConfigurationSection(key)) {
                clearUnused(config.getConfigurationSection(key), defaultConfig.getConfigurationSection(key));
            }
        }
    }
    
    @Nonnull
    public static NamespacedKey getKey(@Nonnull String key) {
        return new NamespacedKey(plugin, key);
    }

    public static void log(@Nonnull Level level, @Nonnull String... logs) {
        for (String log : logs) {
            plugin.getLogger().log(level, log);
        }
    }
    
    public static void log(@Nonnull String... logs) {
        log(Level.INFO, logs);
    }
    
    public static void runSync(@Nonnull Runnable runnable, long delay) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }
    
    public static void scheduleRepeatingSync(@Nonnull Runnable runnable, long interval) {
        scheduleRepeatingSync(runnable, 0, interval);
    }

    public static void scheduleRepeatingSync(@Nonnull Runnable runnable, long delay, long interval) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, interval);
    }

    public static void runSync(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "Cannot run null");

        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    public static void registerListener(@Nonnull Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
    
    public static void startTicker(@Nonnull Runnable onTick) {
        Validate.isTrue(currentTick == 0, "Ticker already started!");
        Validate.notNull(onTick, "Cannot start a null ticker");
        
        scheduleRepeatingSync(() -> {
            long time = System.currentTimeMillis();
            if (currentTick == 6000) {
                currentTick = 1;
            } else {
                currentTick++;
            }
            onTick.run();
            timings = System.currentTimeMillis() - time;
        }, TICKER_DELAY);
    }
    
}
