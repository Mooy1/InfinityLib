package io.github.mooy1.infinitylib;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.logging.Level;

@UtilityClass
public final class PluginUtils {
    
    @Getter private static JavaPlugin plugin = null;
    @Getter private static SlimefunAddon addon = null;
    @Getter private static int currentTick = 0;
    @Getter private static long timings = 0;
    @Getter private static String prefix = null;
    public static final int TICKER_DELAY = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    public static final float TICK_RATIO = 20F / PluginUtils.TICKER_DELAY;

    /**
     * sets up plugin config and starts auto updater
     */
    public static void setup(@Nonnull String prefix, @Nonnull SlimefunAddon addon, @Nonnull String url, @Nonnull File file) {
        PluginUtils.addon = addon;
        
        plugin = addon.getJavaPlugin();
        
        PluginUtils.prefix = ChatColor.GRAY + "[" + prefix + ChatColor.GRAY + "] " + ChatColor.WHITE;
        
        // setup config
        plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true).copyHeader(true);
        plugin.saveConfig();
        
        // auto update
        if (plugin.getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(plugin, file, url).start();
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

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static void scheduleRepeatingSync(@Nonnull Runnable runnable, long delay, long interval) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, interval);
    }

    public static void runSync(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "Cannot run null");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    public static void registerListener(@Nonnull Listener listener) {
        PluginUtils.getPlugin().getServer().getPluginManager().registerEvents(listener, PluginUtils.getPlugin());
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
        }, 10L, TICKER_DELAY);
    }
    
}
