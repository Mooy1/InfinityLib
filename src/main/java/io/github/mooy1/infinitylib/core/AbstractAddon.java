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

    private final boolean notTesting;
    private boolean autoUpdateEnabled;
    private boolean officialBuild;
    private boolean enabled;

    private String autoUpdateKey;
    private String githubUserName;
    private String githubRepo;
    private String githubBranch;
    private String bugTrackerURL;

    private AddonCommand command;
    private AddonConfig config;
    private int tickCount;

    /**
     * Main Constructor
     */
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
    protected AbstractAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.notTesting = false;
    }

    @Override
    public final void onEnable() {
        if (this.enabled) {
            throw new IllegalStateException(getName() + " is already enabled! Do not call super.onEnable()!");
        }

        instance = this;

        this.enabled = true;

        // Validate GitHub path
        if (isNotTesting()) {
            Pattern valid = Pattern.compile("[\\w-]+");
            Validate.isTrue(valid.matcher(this.githubUserName).matches());
            Validate.isTrue(valid.matcher(this.githubRepo).matches());
            Validate.isTrue(valid.matcher(this.githubBranch).matches());

            // Create bug track url
            this.bugTrackerURL = "https://github.com/" + this.githubUserName + "/" + this.githubRepo + "/issues";
        }

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
        this.autoUpdateEnabled = updater != null && this.config.getBoolean(this.autoUpdateKey);

        // Auto update if enabled
        if (this.autoUpdateEnabled) {
            updater.start();
        }

        // Add default commands
        PluginCommand command = getCommand(getName());
        if (command == null) {
            throw new IllegalStateException("Add a command with the same name as your addon to your plugin.yml!");
        }
        this.command = new AddonCommand(command);

        // Create total tick count
        Tasks.repeat(Slimefun.getTickerTask().getTickRate(), () -> this.tickCount++);

        // Enable
        try {
            enable();
        } catch (Throwable e) {
            if (isNotTesting()) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void onDisable() {
        if (!this.enabled) {
            throw new IllegalStateException(getName() + " is already disabled! Do not call super.onDisable()!");
        }
        this.enabled = false;

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
     * Adds a sub command to the command of the same name as this addon
     */
    protected final void addCommand(SubCommand subCommand) {
        this.command.addCommand(subCommand);
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

    @SuppressWarnings("unchecked")
    public static <T extends AbstractAddon> T instance() {
        return Objects.requireNonNull((T) AbstractAddon.instance, "Addon is not enabled!");
    }

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
     * Returns whether auto updates are enabled
     */
    public static boolean areAutoUpdatesEnabled() {
        return instance.autoUpdateEnabled;
    }

    /**
     * Creates a NameSpacedKey from the given string
     */
    public static NamespacedKey makeKey(String s) {
        return new NamespacedKey(instance(), s);
    }

}
