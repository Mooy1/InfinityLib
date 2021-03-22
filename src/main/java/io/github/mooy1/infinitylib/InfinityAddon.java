package io.github.mooy1.infinitylib;

import io.github.mooy1.infinitylib.command.AbstractCommand;
import io.github.mooy1.infinitylib.command.CommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Extend this in your main plugin class and add static instance getter
 */
public abstract class InfinityAddon extends JavaPlugin implements SlimefunAddon, Listener {

    public static final int TICKER_DELAY = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    public static final float TICK_RATIO = 20F / TICKER_DELAY;
    
    @Getter
    private int globalTick = 0;
    
    @Getter(AccessLevel.PROTECTED)
    private Metrics metrics;
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void onEnable() {

        // copy config if not present
        saveDefaultConfig();

        // add auto update
        Objects.requireNonNull(getConfig().getDefaults()).set("auto-update", true);

        // remove unused fields in config
        for (String key : getConfig().getKeys(true)) {
            if (!getConfig().getDefaults().contains(key)) {
                getConfig().set(key, null);
            }
        }

        // copy defaults and header to update stuff
        getConfig().options().copyDefaults(true).copyHeader(true);

        // save
        saveConfig();

        // auto update
        if (getConfig().getBoolean("auto-update")) {
            if (getDescription().getVersion().startsWith("DEV - ")) {
                new GitHubBuildsUpdater(this, getFile(), getGithubPath()).start();
            }
        } else {
            runSync(() -> log(
                    "#######################################",
                    "Auto Updates have been disabled for " + getName(),
                    "You will receive no support for bugs",
                    "Until you update to the latest version!",
                    "#######################################"
            ));
        }

        // global ticker
        scheduleRepeatingSync(() -> this.globalTick++, TICKER_DELAY);

        // metrics
        if (getMetricsID() != -1) {
            this.metrics = new Metrics(this, getMetricsID());
            this.metrics.addCustomChart(new Metrics.SimplePie("auto_updates", () -> String.valueOf(getConfig().getBoolean("auto-update"))));
        }
        
        // commands
        new CommandManager(this, getCommand(getName().toLowerCase(Locale.ROOT)), getCommands());
        
    }

    /**
     * return your metrics id or -1
     */
    protected abstract int getMetricsID();

    /**
     * return the github path in the format user/repo/branch, for example Mooy1/InfinityExpansion/master
     */
    protected abstract String getGithubPath();

    /**
     * return your sub commands, you should have a command with the same name as plugin in your plugin.yml
     */
    protected abstract AbstractCommand[] getCommands();
    
    @Nonnull
    @Override
    public final JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public final String getBugTrackerURL() {
        return "https://github.com/" + getGithubPath() + "/issues";
    }

    public final void log(String... messages) {
        log(Level.INFO, messages);
    }

    public final void log(Level level, String... messages) {
        for (String msg : messages) {
            getLogger().log(level, msg);
        }
    }

    public final void broadcast(String message) {
        Bukkit.broadcastMessage(ChatColors.color("&7[&b" + getName() + "&7] &f" + message));
    }

    public final void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public final void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(this, runnable);
    }

    public final void runSync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(this, runnable, delay);
    }

    public final void scheduleRepeatingSync(Runnable runnable, long interval) {
        Bukkit.getScheduler().runTaskTimer(this, runnable, 0, interval);
    }

    public final void scheduleRepeatingSync(Runnable runnable, long delay, long interval) {
        Bukkit.getScheduler().runTaskTimer(this, runnable, delay, interval);
    }

    public final void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
    }

    public final void runAsync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, delay);
    }

    public final void scheduleRepeatingAsync(Runnable runnable, long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, 0, interval);
    }

    public final void scheduleRepeatingAsync(Runnable runnable, long delay, long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, delay, interval);
    }

    @Nonnull
    public final Config attachDefaults(Config config, String resource) {
        config.getConfiguration().setDefaults(YamlConfiguration.loadConfiguration(
                new InputStreamReader(Objects.requireNonNull(getResource(resource),
                        () -> "Failed to get default resource " + resource + "!"))));
        config.getConfiguration().options().copyDefaults(true).copyHeader(true);
        config.save();
        return config;
    }

}
