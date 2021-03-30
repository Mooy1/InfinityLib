package io.github.mooy1.infinitylib;

import io.github.mooy1.infinitylib.commands.AbstractCommand;
import io.github.mooy1.infinitylib.slimefun.utils.TickerUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Extend this in your main plugin class and add static instance getter
 */
public abstract class AbstractAddon extends JavaPlugin implements SlimefunAddon {
    
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
        scheduleRepeatingSync(() -> this.globalTick++, TickerUtils.TICKS);

        // metrics
        if (getMetricsID() != -1) {
            this.metrics = new Metrics(this, getMetricsID());
            this.metrics.addCustomChart(new Metrics.SimplePie("auto_updates", () -> String.valueOf(getConfig().getBoolean("auto-update"))));
        }
        
        // commands
        PluginCommand command = getCommand(getName().toLowerCase(Locale.ROOT));
        if (command != null) {
            List<AbstractCommand> commands = new ArrayList<>(getSubCommands());
            commands.add(new AddonInfoCommand(this));
            new CommandHelper(command, commands);
        }
    }

    /**
     * return your metrics id or -1 for none
     */
    protected abstract int getMetricsID();

    /**
     * return the github path in the format user/repo/branch, for example Mooy1/InfinityExpansion/master
     */
    protected abstract String getGithubPath();

    /**
     * return your sub commands, use Arrays.asList(Commands...)
     */
    protected abstract List<AbstractCommand> getSubCommands();
    
    @Nonnull
    @Override
    public final JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public final String getBugTrackerURL() {
        return "https://github.com/" + getGithubPath().substring(0, getGithubPath().lastIndexOf('/')) + "/issues";
    }

    public final void log(String... messages) {
        log(Level.INFO, messages);
    }

    public final void log(Level level, String... messages) {
        for (String msg : messages) {
            getLogger().log(level, msg);
        }
    }

    public final void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
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
    
    public final void addSubCommands(String command, AbstractCommand... commands) {
        new CommandHelper(Objects.requireNonNull(getCommand(command), () -> "No such command '" + command + "'!"), new ArrayList<>(Arrays.asList(commands)));
    }

    public final NamespacedKey getKey(String s) {
        return new NamespacedKey(this, s);
    }
    
    public final Config loadConfig(String name) {
        return new Config(this, name);
    }
    
    public final Config loadConfigWithDefaults(String name) {
        return attachConfigDefaults(loadConfig(name), name);
    }

    public final Config attachConfigDefaults(Config config, String resource) {
        config.getConfiguration().setDefaults(YamlConfiguration.loadConfiguration(
                new InputStreamReader(Objects.requireNonNull(getResource(resource),
                        () -> "Failed to get default resource " + resource + "!"))));
        config.getConfiguration().options().copyDefaults(true).copyHeader(true);
        config.save();
        return config;
    }
    
    public final int getConfigInt(String path, int min, int max) {
        int val = getConfig().getInt(path);
        if (val < min || val > max) {
            getConfig().set(path, val = Objects.requireNonNull(getConfig().getDefaults()).getInt(path));
            log(Level.WARNING, "Config value at " + path + " was out of bounds, resetting to default!");
        }
        return val;
    }

    public final double getConfigDouble(String path, double min, double max) {
        double val = getConfig().getDouble(path);
        if (val < min || val > max) {
            getConfig().set(path, val = Objects.requireNonNull(getConfig().getDefaults()).getDouble(path));
            log(Level.WARNING, "Config value at " + path + " was out of bounds, resetting to default!");
        }
        return val;
    }
    
    private static final class CommandHelper implements TabExecutor {
        
        private final Map<String, AbstractCommand> commands = new HashMap<>();
        
        private CommandHelper(PluginCommand command, List<AbstractCommand> abstractCommands) {
            command.setExecutor(this);
            command.setTabCompleter(this);
            abstractCommands.add(new HelpCommand(command));
            for (AbstractCommand abstractCommand : abstractCommands) {
                this.commands.put(abstractCommand.name, abstractCommand);
            }
        }
    
        @Override
        public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
            if (args.length > 0) {
                AbstractCommand command1 = this.commands.get(args[0]);
                if (command1 != null && command1.hasPerm(sender)) {
                    command1.onExecute(sender, args);
                    return true;
                }
            }
            return false;
        }
    
        @Override
        public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
            if (args.length == 1) {
                List<String> strings = new ArrayList<>();
                for (AbstractCommand command1 : this.commands.values()) {
                    if (command1.hasPerm(sender)) {
                        strings.add(command1.name);
                    }
                }
                return createReturnList(strings, args[0]);
            } else if (args.length > 1) {
                AbstractCommand command1 = this.commands.get(args[0]);
                if (command1 != null && command1.hasPerm(sender)) {
                    List<String> strings = new ArrayList<>();
                    command1.onTab(sender, args, strings);
                    return createReturnList(strings, args[args.length - 1]);
                }
            }
            return Collections.emptyList();
        }
    
        @Nonnull
        private static List<String> createReturnList(@Nonnull List<String> strings, @Nonnull String string) {
            String input = string.toLowerCase(Locale.ROOT);
            List<String> returnList = new LinkedList<>();
            for (String item : strings) {
                if (item.toLowerCase(Locale.ROOT).contains(input)) {
                    returnList.add(item);
                    if (returnList.size() >= 64) {
                        break;
                    }
                } else if (item.equalsIgnoreCase(input)) {
                    return Collections.emptyList();
                }
            }
            return returnList;
        }

        private final class HelpCommand extends AbstractCommand implements Listener {

            private final String command;
            private final String help;
            private final String header;
            private final String aliases;

            private HelpCommand(PluginCommand command) {
                super("help", "Displays this", false);
                
                Bukkit.getPluginManager().registerEvents(this, command.getPlugin());
                
                this.help = "/help " + command.getName();
                this.command = ChatColor.GOLD + "/" + command.getName() + " ";
                this.aliases = ChatColors.color("&6Aliases: &e" + command.getAliases());
                this.header = ChatColors.color("&7----------&b " + command.getPlugin().getName() + " Help &7----------");
            }

            @Override
            public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
                sender.sendMessage("");
                sender.sendMessage(this.header);
                sender.sendMessage("");
                for (AbstractCommand command : CommandHelper.this.commands.values()) {
                    if (command.hasPerm(sender)) {
                        sender.sendMessage(this.command + command.name + ChatColor.YELLOW + " - " + command.description);
                    }
                }
                sender.sendMessage("");
                sender.sendMessage(this.aliases);
                sender.sendMessage("");
            }

            @Override
            public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {

            }

            @EventHandler
            public void onCommand(PlayerCommandPreprocessEvent e) {
                if (e.getMessage().equalsIgnoreCase(this.help)) {
                    onExecute(e.getPlayer(), new String[0]);
                    e.setCancelled(true);
                }
            }

        }
        
    }

    private static final class AddonInfoCommand extends AbstractCommand {

        private final String[] message;
        
        private AddonInfoCommand(AbstractAddon addon) {
            super("info", "Gives addon version information", false);
            this.message = new String[] {
                    "",
                    ChatColors.color("&b" + addon.getName() + " Info"),
                    ChatColors.color("&bSlimefun Version: &7" + Objects.requireNonNull(SlimefunPlugin.instance()).getPluginVersion()),
                    ChatColors.color("&bSlimefun Discord: &7Discord.gg/slimefun"),
                    ChatColors.color("&bAddon Version: &7" + addon.getPluginVersion()),
                    ChatColors.color("&bAddon Community: &7Discord.gg/SqD3gg5SAU"),
                    ChatColors.color("&bGithub: &7" + addon.getBugTrackerURL()),
                    ""
            };
        }

        @Override
        public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
            sender.sendMessage(this.message);
        }

        @Override
        public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {

        }

    }
    
}
