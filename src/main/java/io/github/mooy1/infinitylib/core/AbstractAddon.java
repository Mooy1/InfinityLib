package io.github.mooy1.infinitylib.core;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.utils.Tasks;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

/**
 * Extend this in your main plugin class to access a bunch of utilities
 *
 * @author Mooy1
 */
@ParametersAreNonnullByDefault
public abstract class AbstractAddon extends JavaPlugin implements SlimefunAddon {

    private static AbstractAddon instance;

    private final String githubUserName;
    private final String githubRepo;
    private final String githubBranch;
    private final String autoUpdateKey;
    private final boolean notTesting;

    private boolean autoUpdatesEnabled;
    private boolean officialBuild;
    private String bugTrackerURL;
    private boolean disabling;

    private ParentCommand command;
    private AddonConfig config;
    private int tickCount;

    /**
     * Main Constructor
     */
    @SuppressWarnings("unused")
    public AbstractAddon(String githubUserName, String githubRepo, String githubBranch, @Nullable String autoUpdateKey) {
        this.notTesting = true;
        this.githubUserName = githubUserName;
        this.githubBranch = githubBranch;
        this.githubRepo = githubRepo;
        this.autoUpdateKey = autoUpdateKey;
    }

    /**
     * MockBukkit Constructor
     */
    protected AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
                            String githubUserName, String githubRepo, String githubBranch, @Nullable String autoUpdateKey) {
        super(loader, description, dataFolder, file);
        this.notTesting = false;
        this.githubUserName = githubUserName;
        this.githubBranch = githubBranch;
        this.githubRepo = githubRepo;
        this.autoUpdateKey = autoUpdateKey;
    }

    @Override
    public final void onEnable() {
        if (instance != null) {
            throw new IllegalStateException(getName() + " is already enabled! Do not call super.onEnable()! Use your own InfinityLib!");
        }

        // Set static instance
        instance = this;

        // Validate GitHub path
        Pattern valid = Pattern.compile("[\\w-]+");
        Validate.isTrue(valid.matcher(this.githubUserName).matches());
        Validate.isTrue(valid.matcher(this.githubRepo).matches());
        Validate.isTrue(valid.matcher(this.githubBranch).matches());

        // Create bug track url
        this.bugTrackerURL = "https://github.com/" + this.githubUserName + "/" + this.githubRepo + "/issues";

        // Check if it's an official build
        this.officialBuild = getDescription().getVersion().matches("DEV - \\d+ \\(git \\w+\\)");

        // Check if it can auto update
        GitHubBuildsUpdater updater = !isOfficialBuild() || this.autoUpdateKey == null ? null :
                new GitHubBuildsUpdater(this, getFile(),
                        this.githubUserName + "/" + this.githubRepo + "/" + this.githubBranch);

        // Create Config
        try {
            this.config = new AddonConfig("config.yml");
        } catch (Throwable e) {
            // There was an error within the config code, attempt auto update to fix this
            e.printStackTrace();
            if (updater != null) {
                updater.start();
                updater = null;
            }
        }

        // Check if auto updates are enabled
        this.autoUpdatesEnabled = updater != null && this.config.getBoolean(this.autoUpdateKey);

        // Auto update if enabled
        if (this.autoUpdatesEnabled) {
            updater.start();
        }

        // Make sure config has an auto update key available to the user
        if (this.autoUpdateKey != null && !this.config.getDefaults().contains(this.autoUpdateKey)) {
            throw new IllegalStateException("Tell the dev to add their auto update key to the config! (Maybe a comment too)");
        }

        // Get plugin command
        PluginCommand command = getCommand(getName());
        if (command == null) {
            throw new IllegalStateException("Tell the dev to add a command with the same name as the addon to the plugin.yml!");
        }

        // Add default commands
        this.command = new AddonCommand(command)
            .addSub(new InfoCommand(this))
            .addSub(new AliasesCommand(command));

        // Create total tick count
        Tasks.repeat(Slimefun.getTickerTask().getTickRate(), () -> this.tickCount++);

        // Call addon enable
        try {
            enable();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onDisable() {
        if (this.disabling) {
            throw new IllegalStateException(getName() + " is already disabled! Do not call super.onDisable()!");
        }

        this.disabling = true;

        Bukkit.getScheduler().cancelTasks(this);

        try {
            disable();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.disabling = false;

        instance = null;
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
        return this.command;
    }

    /**
     * Returns whether auto updates are enabled, for use in metrics
     */
    protected final boolean autoUpdatesEnabled() {
        return this.autoUpdatesEnabled;
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
        // Do nothing, it's covered in onEnable()
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T extends AbstractAddon> T instance() {
        return (T) Objects.requireNonNull(AbstractAddon.instance, "Addon is not enabled!");
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
    public static NamespacedKey makeKey(String s) {
        return new NamespacedKey(instance(), s);
    }

}
