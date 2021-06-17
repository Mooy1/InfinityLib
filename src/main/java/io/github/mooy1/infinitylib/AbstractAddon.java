package io.github.mooy1.infinitylib;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.commands.AbstractCommand;
import io.github.mooy1.infinitylib.commands.CommandUtils;
import io.github.mooy1.infinitylib.configuration.AddonConfig;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;

/**
 * Extend this in your main plugin class to access a bunch of utilities
 *
 * @author Mooy1
 */
public abstract class AbstractAddon extends JavaPlugin implements SlimefunAddon {

    private final boolean notTesting;
    private boolean officialBuild;
    private String bugTrackerURL;
    private AddonConfig config;
    private int globalTick;

    /**
     * Main Constructor
     */
    public AbstractAddon() {
        this.notTesting = true;
    }

    /**
     * MockBukkit Constructor
     */
    @ParametersAreNonnullByDefault
    public AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.notTesting = false;
    }

    @Override
    public final void onEnable() {

        // Check path
        String githubPath = getGithubPath();
        if (!githubPath.matches("\\w+/\\w+/\\w+")) {
            throw new IllegalStateException("Invalid Github Path '" + githubPath + "', It should be 'User/Repo/branch'!");
        }

        // Create bug track url
        String userSlashRepo = githubPath.substring(0, githubPath.lastIndexOf('/'));
        this.bugTrackerURL = "https://github.com/" + userSlashRepo + "/issues";

        // Check if its an official build
        this.officialBuild = getDescription().getVersion().matches("DEV - \\d+ \\(git \\w+\\)");

        // Create Config
        try {
            this.config = new AddonConfig(this, "config.yml");
        } catch (Throwable e) {
            e.printStackTrace();
            if (isOfficialBuild() && getAutoUpdatePath() != null) {
                new GitHubBuildsUpdater(this, getFile(), githubPath).start();
            }
            return;
        }

        // Don't do metrics or updates unless its an official build
        if (isOfficialBuild()) {

            // Setup Metrics
            Metrics metrics = null;
            try {
                metrics = setupMetrics();
            } catch (Throwable e) {
                if (isNotTesting()) {
                    e.printStackTrace();
                }
            }

            if (getAutoUpdatePath() != null) {
                boolean autoUpdate = this.config.getBoolean(getAutoUpdatePath());

                // Add a metrics chart
                if (metrics != null) {
                    String autoUpdates = String.valueOf(autoUpdate);
                    metrics.addCustomChart(new SimplePie("auto_updates", () -> autoUpdates));
                }

                // Update
                if (autoUpdate) {
                    new GitHubBuildsUpdater(this, getFile(), githubPath).start();
                }
            }
        }

        // Enable
        try {
            enable();
        } catch (Throwable e) {
            if (isNotTesting()) {
                e.printStackTrace();
            }
        }

        // Setup commands after enable so that addon can setup stuff
        try {
            List<AbstractCommand> subCommands = setupSubCommands();
            if (subCommands != null) {
                CommandUtils.addSubCommands(this, getCommandName(), subCommands);
            }
        } catch (Throwable e) {
            if (isNotTesting()) {
                e.printStackTrace();
            }
        }

        // Create Global Ticker
        scheduleRepeatingSync(() -> this.globalTick++, SlimefunPlugin.getTickerTask().getTickRate());
    }

    @Override
    public final void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        disable();
    }

    /**
     * Called when the plugin is enabled
     */
    protected abstract void enable();

    /**
     * Called when the plugin is disabled
     */
    protected abstract void disable();

    /**
     * return the auto update path in the format user/repo/branch, for example Mooy1/InfinityExpansion/master
     */
    @Nonnull
    protected abstract String getGithubPath();

    /**
     * Return your metrics or null
     */
    @Nullable
    protected Metrics setupMetrics() {
        return null;
    }

    /**
     * return your sub commands, use Arrays.asList(Commands...) or null for none.
     */
    @Nullable
    protected List<AbstractCommand> setupSubCommands() {
        return null;
    }

    /**
     * return your main command name, defaults to the name of the plugin
     */
    @Nonnull
    protected String getCommandName() {
        return getName();
    }

    /**
     * Override this if you have a different path, or null for no auto updates
     */
    @Nullable
    public abstract String getAutoUpdatePath();

    public final int getGlobalTick() {
        return this.globalTick;
    }

    public final boolean isNotTesting() {
        return this.notTesting;
    }

    public final boolean isOfficialBuild() {
        return this.officialBuild;
    }

    @Nonnull
    @Override
    public final JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public final String getBugTrackerURL() {
        return this.bugTrackerURL;
    }

    @Nonnull
    @Override
    public final AddonConfig getConfig() {
        return this.config;
    }

    @Override
    public final void reloadConfig() {
        this.config.reload();
    }

    @Override
    public final void saveConfig() {
        this.config.save();
    }

    @Override
    public final void saveDefaultConfig() {
        // Do nothing, its covered in onEnable()
    }

    public final void log(String... messages) {
        log(Level.INFO, messages);
    }

    public final void log(Level level, String... messages) {
        Logger logger = getLogger();
        for (String msg : messages) {
            logger.log(level, msg);
        }
    }

    public final void registerListener(Listener... listeners) {
        PluginManager manager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }
    }

    public final NamespacedKey getKey(String s) {
        return new NamespacedKey(this, s);
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

}
