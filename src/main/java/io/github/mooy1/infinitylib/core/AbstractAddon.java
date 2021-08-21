package io.github.mooy1.infinitylib.core;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.commands.AddonCommand;
import io.github.mooy1.infinitylib.commands.ParentCommand;
import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

/**
 * Extend this in your main plugin class to access a bunch of utilities
 *
 * @author Mooy1
 */
public abstract class AbstractAddon extends JavaPlugin implements SlimefunAddon {

    private static AbstractAddon instance;

    private final String autoUpdateKey;
    private final String githubUserName;
    private final String githubRepo;
    private final String autoUpdateBranch;
    private final boolean notTesting;

    private boolean autoUpdatesEnabled;
    private boolean officialBuild;
    private String bugTrackerURL;
    private boolean disabling;
    private boolean loading;

    private ParentCommand command;
    private AddonConfig config;
    private int tickCount;

    /**
     * Subclass Main Constructor
     */
    @SuppressWarnings("unused")
    public AbstractAddon(String githubUserName, String githubRepo, String autoUpdateBranch, String autoUpdateKey) {
        this.notTesting = true;
        this.githubUserName = githubUserName;
        this.autoUpdateBranch = autoUpdateBranch;
        this.githubRepo = githubRepo;
        this.autoUpdateKey = autoUpdateKey;
    }

    /**
     * Subclass Testing Constructor
     */
    protected AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
                            String githubUserName, String githubRepo, String autoUpdateBranch, String autoUpdateKey) {
        super(loader, description, dataFolder, file);
        this.notTesting = false;
        this.githubUserName = githubUserName;
        this.autoUpdateBranch = autoUpdateBranch;
        this.githubRepo = githubRepo;
        this.autoUpdateKey = autoUpdateKey;
    }

    /**
     * InfinityLib Testing Constructor
     */
    // TODO implement tests
    AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        notTesting = false;
        githubUserName = "Mooy1";
        autoUpdateBranch = "InfinityLib";
        githubRepo = "master";
        autoUpdateKey = "auto-update";
    }

    @Override
    public final void onLoad() {
        if (loading) {
            throw new IllegalStateException(getName() + " is already loaded! Do not call super.onLoad()! Shade your own InfinityLib");
        }

        loading = true;

        try {
            load();
        } catch (RuntimeException e) {
            throwIfAddonTest(e);
        } finally {
            loading = false;
        }
    }

    @Override
    public final void onEnable() {
        if (instance != null) {
            throw new IllegalStateException(getName() + " is already enabled! Do not call super.onEnable()!");
        }

        // Set static instance
        instance = this;

        // Validate GitHub strings
        Pattern validGithubString = Pattern.compile("[\\w-]+");
        Validate.isTrue(validGithubString.matcher(githubUserName).matches(), "Invalid githubUserName");
        Validate.isTrue(validGithubString.matcher(githubRepo).matches(), "Invalid githubRepo");
        Validate.isTrue(validGithubString.matcher(autoUpdateBranch).matches(), "Invalid autoUpdateBranch");

        // Create bug track url
        bugTrackerURL = "https://github.com/" + githubUserName + "/" + githubRepo + "/issues";

        // Check if it's an official build
        officialBuild = getDescription().getVersion().matches("DEV - \\d+ \\(git \\w+\\)");

        // Check if it can auto update
        GitHubBuildsUpdater updater = isOfficialBuild() ? new GitHubBuildsUpdater(this, getFile(),
                githubUserName + "/" + githubRepo + "/" + autoUpdateBranch) : null;

        // This is used to mark when the config is broken, so we should always auto update
        boolean brokenConfig = false;

        // Create Config
        try {
            config = new AddonConfig("config.yml");
        } catch (RuntimeException e) {
            brokenConfig = true;
            e.printStackTrace();
        }

        // Validate configAutoUpdateKey
        if (autoUpdateKey == null) {
            brokenConfig = true;
            throwIfAddonTest(new IllegalStateException("Null auto update key"));
        } else if (autoUpdateKey.isEmpty()) {
            brokenConfig = true;
            throwIfAddonTest(new IllegalStateException("Empty auto update key!"));
        } else if (!config.getDefaults().contains(autoUpdateKey, true)) {
            brokenConfig = true;
            throwIfAddonTest(new IllegalStateException("Auto update key missing from the default config!"));
        }

        // Auto update if enabled
        if (updater != null) {
            if (brokenConfig) {
                updater.start();
            } else if (config.getBoolean(autoUpdateKey)) {
                autoUpdatesEnabled = true;
                updater.start();
            }
        }

        // Get plugin command
        PluginCommand pluginCommand = getCommand(getName());
        if (pluginCommand == null) {
            throwIfAddonTest(new IllegalStateException("Command named '" + getName() + "' missing from plugin.yml!"));
        } else {
            command = new AddonCommand(pluginCommand);
        }

        // Create total tick count
        Scheduler.repeat(Slimefun.getTickerTask().getTickRate(), () -> tickCount++);

        // Call addon enable
        try {
            enable();
        } catch (RuntimeException e) {
            throwIfAddonTest(e);
        }
    }

    @Override
    public final void onDisable() {
        if (disabling) {
            throw new IllegalStateException(getName() + " is already disabled! Do not call super.onDisable()!");
        }

        disabling = true;

        Bukkit.getScheduler().cancelTasks(this);

        try {
            disable();
        } catch (RuntimeException e) {
            throwIfAddonTest(e);
        } finally {
            disabling = false;
            instance = null;
        }
    }

    /**
     * Throws an exception if in a test environment, otherwise just logs the stacktrace so that the plugin functions
     */
    private void throwIfAddonTest(RuntimeException e) {
        if (notTesting) {
            e.printStackTrace();
        } else {
            throw e;
        }
    }

    /**
     * Called when the plugin is loaded
     */
    protected void load() {

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
     * Gets the command of the same name as this addon
     */
    @Nonnull
    protected final ParentCommand getCommand() {
        return command;
    }

    /**
     * Returns whether auto updates are enabled, for use in metrics
     */
    protected final boolean autoUpdatesEnabled() {
        return autoUpdatesEnabled;
    }

    @Nonnull
    @Override
    public final JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public final String getBugTrackerURL() {
        return bugTrackerURL;
    }

    @Nonnull
    @Override
    public final AddonConfig getConfig() {
        return config;
    }

    @Override
    public final void reloadConfig() {
        config.reload();
    }

    @Override
    public final void saveConfig() {
        config.save();
    }

    @Override
    public final void saveDefaultConfig() {
        // Do nothing, it's covered in onEnable()
    }

    @Nonnull
    @Override
    public final String getPluginVersion() {
        return getDescription().getVersion();
    }

    @Override
    public final boolean hasDependency(String dependency) {
        return SlimefunAddon.super.hasDependency(dependency);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T extends AbstractAddon> T instance() {
        return (T) Objects.requireNonNull(instance, "Addon is not enabled!");
    }

    @Nonnull
    public static AddonConfig config() {
        return instance().getConfig();
    }

    public static void log(Level level, String... messages) {
        Logger logger = instance().getLogger();
        for (String msg : messages) {
            logger.log(level, msg);
        }
    }

    /**
     * Returns the total number of Slimefun ticks that have occurred
     */
    public static int tickCount() {
        return instance().tickCount;
    }

    /**
     * Returns whether the plugin is currently not in a testing environment
     */
    public static boolean isNotTesting() {
        return instance().notTesting;
    }

    /**
     * Returns whether the plugin is an official build
     */
    public static boolean isOfficialBuild() {
        return instance().officialBuild;
    }

    /**
     * Creates a NameSpacedKey from the given string
     */
    @Nonnull
    public static NamespacedKey createKey(String s) {
        return new NamespacedKey(instance(), s);
    }

}
