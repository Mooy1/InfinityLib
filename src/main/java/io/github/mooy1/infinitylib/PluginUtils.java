package io.github.mooy1.infinitylib;

import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Locale;
import java.util.logging.Level;

@UtilityClass
public final class PluginUtils {
    
    @Getter
    private static JavaPlugin plugin = null;
    @Getter
    private static int currentTick = 0;
    @Getter
    private static long timings = 0;
    public static final int TICKER_DELAY = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    public static final float TICK_RATIO = 20F / PluginUtils.TICKER_DELAY;

    /**
     * sets up plugin config and starts auto updater
     */
    public static void setup(@Nonnull String prefix, @Nonnull JavaPlugin javaPlugin, @Nonnull String url, @Nonnull File file) {
        plugin = javaPlugin;
        MessageUtils.prefix = ChatColor.GRAY + "[" + prefix + ChatColor.GRAY + "] " + ChatColor.WHITE;
        javaPlugin.saveDefaultConfig();
        javaPlugin.getConfig().options().copyDefaults(true).copyHeader(true);
        javaPlugin.saveConfig();
        if (javaPlugin.getDescription().getVersion().startsWith("DEV - ")) {
            log(Level.INFO, "Starting auto update");
            new GitHubBuildsUpdater(plugin, file, url).start();
        } else {
            log(Level.WARNING, "You must be on a DEV build to auto update!");
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
    
    public static void registerAddonInfoItem(Category category, SlimefunAddon addon) {
        new SlimefunItem(category, new SlimefunItemStack(
                addon.getName().toUpperCase(Locale.ROOT) + "_ADDON_INFO",
                Material.NETHER_STAR,
                "&bAddon Info",
                "&fVersion: &7" + addon.getPluginVersion(),
                "",
                "&fDiscord: &b@&7Riley&8#5911",
                "&7discord.gg/slimefun",
                "",
                "&fGithub: &b@&8&7Mooy1",
                "&7" + addon.getBugTrackerURL()
        ), RecipeType.NULL, null).register(addon);
    }

    public static void registerEvents(@Nonnull Listener listener) {
        PluginUtils.getPlugin().getServer().getPluginManager().registerEvents(listener, PluginUtils.getPlugin());
    }
    
    private static void validate() {
        Validate.notNull(plugin, "Make sure to set the plugin instance");
    }
    
    public static void startTicker() {
        Validate.isTrue(currentTick == 0, "Ticker already started!");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long time = System.currentTimeMillis();
            if (currentTick < 600) {
                currentTick++;
            } else {
                currentTick = 1;
            }
            timings = System.currentTimeMillis() - time;
        }, 10L, PluginUtils.TICKER_DELAY);
    }

    public static void startTicker(@Nonnull Runnable onTick) {
        Validate.isTrue(currentTick == 0, "Ticker already started!");
        Validate.notNull(onTick, "Cannot start a null ticker");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long time = System.currentTimeMillis();
            if (currentTick < 600) {
                currentTick++;
            } else {
                currentTick = 1;
            }
            onTick.run();
            timings = System.currentTimeMillis() - time;
        }, 10L, PluginUtils.TICKER_DELAY);
    }
    
}
