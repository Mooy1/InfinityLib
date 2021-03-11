package io.github.mooy1.infinitylib.command;

import io.github.mooy1.infinitylib.core.PluginUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Manages command stuff for your addon, register {@link AbstractCommand}s to this
 * 
 * @author Mooy1
 */
public final class CommandManager implements CommandExecutor, TabCompleter {

    public static CommandManager setup(String command, String permission, String aliases, AbstractCommand... commands) {
        CommandManager manager = new CommandManager(Objects.requireNonNull(PluginUtils.getPlugin().getCommand(command)), permission, aliases);
        for (AbstractCommand command1 : commands) {
            manager.addCommand(command1);
        }
        return manager;
    }
    
    public static final int MAX_TAB_COMPLETE = 64;
    
    private static final Info INFO_COMMAND_INSTANCE = new Info();
    private static final String PLUGIN_HELP_HEADER = ChatColors.color("&7----------&b&l " + PluginUtils.getPlugin().getName() + " &7----------");
    
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    
    private final List<String> commandNames = new ArrayList<>();
    private final List<String> defaultCommandNames = new ArrayList<>();

    private final List<String> commandDescriptions = new ArrayList<>();
    private final List<String> defaultCommandDescriptions = new ArrayList<>();
    
    private final String permission;
    private final String aliases;
    private final String command;
    
    private CommandManager(PluginCommand command, String permission, String aliases) {
        this.permission = permission;
        this.aliases = ChatColors.color("&6Aliases: &e" + aliases);
        this.command = command.getName();
        
        command.setExecutor(this);
        command.setTabCompleter(this);
        
        addCommand(INFO_COMMAND_INSTANCE);
        addCommand(new Help());
    }
    
    public void addCommand(AbstractCommand command) {
        this.commands.put(command.getName(), command);
        this.commandNames.add(command.getName());
        String desc = toDescription(command);
        this.commandDescriptions.add(desc);
        if (!command.isOp()) {
            this.defaultCommandNames.add(command.getName());
            this.defaultCommandDescriptions.add(desc);
        }
    }
    
    private boolean hasPerm(AbstractCommand command, CommandSender sender) {
        return command != null && (!command.isOp() || hasPerm(sender));
    }
    
    private boolean hasPerm(CommandSender sender) {
        return sender.isOp() || sender.hasPermission(this.permission);
    }
    
    private String toDescription(AbstractCommand command) {
        return ChatColors.color("&6/" + CommandManager.this.command + " " + command.getName() + " &e- " + command.getDescription());
    }
    
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length > 0) {
            AbstractCommand command1 = this.commands.get(args[0]);
            if (hasPerm(command1, sender)) {
                command1.onExecute(sender, args);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
        if (args.length == 1) {
            if (hasPerm(sender)) {
                return createReturnList(this.commandNames, args[0]);
            } else {
                return createReturnList(this.defaultCommandNames, args[0]);
            }
        } else if (args.length > 1) {
            AbstractCommand command1 = this.commands.get(args[0]);
            if (hasPerm(command1, sender)) {
                return createReturnList(command1.onTab(sender, args), args[args.length - 1]);
            }
        }
        return new ArrayList<>();
    }

    @Nonnull
    private static List<String> createReturnList(@Nonnull List<String> list, @Nonnull String string) {
        if (string.length() == 0) {
            return list;
        }
        String input = string.toLowerCase(Locale.ROOT);
        List<String> returnList = new LinkedList<>();
        for (String item : list) {
            if (item.toLowerCase(Locale.ROOT).contains(input)) {
                returnList.add(item);
                if (returnList.size() >= MAX_TAB_COMPLETE) {
                    break;
                }
            } else if (item.equalsIgnoreCase(input)) {
                return Collections.emptyList();
            }
        }
        return returnList;
    }

    private final class Help extends AbstractCommand implements Listener {
        private final String help;
        
        private Help() {
            super("help", "Displays this", false);
            this.help = "/help " + CommandManager.this.command;
            PluginUtils.registerListener(this);
        }
    
        @Override
        public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
            sender.sendMessage("");
            sender.sendMessage(PLUGIN_HELP_HEADER);
            sender.sendMessage("");
            if (hasPerm(sender)) {
                for (String s : CommandManager.this.commandDescriptions) {
                    sender.sendMessage(s);
                }
            } else {
                for (String s : CommandManager.this.defaultCommandDescriptions) {
                    sender.sendMessage(s);
                }
            }
            sender.sendMessage("");
            sender.sendMessage(CommandManager.this.aliases);
            sender.sendMessage("");
        }
    
        @Nonnull
        @Override
        public List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args) {
            return Collections.emptyList();
        }

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent e) {
            if (e.getMessage().equalsIgnoreCase(this.help)) {
                onExecute(e.getPlayer(), new String[0]);
                e.setCancelled(true);
            }
        }
    
    }

    private static final class Info extends AbstractCommand {

        private static final String[] INFO = {
                "",
                ChatColors.color("&aSlimefun Version: " + Objects.requireNonNull(SlimefunPlugin.instance()).getPluginVersion()),
                ChatColors.color("&bSlimefun Discord: &7Discord.gg/slimefun"),
                ChatColors.color("&aAddon Version: " + PluginUtils.getAddon().getPluginVersion()),
                ChatColors.color("&bAddon Community: &7Discord.gg/SqD3gg5SAU"),
                ChatColors.color("&aGithub: &7" + PluginUtils.getAddon().getBugTrackerURL()),
                ""
        };
        
        private Info() {
            super("info", "Gives version information", false);
        }

        @Override
        public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
            sender.sendMessage(INFO);
        }

        @Nonnull
        @Override
        public List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args) {
            return Collections.emptyList();
        }

    }

}
