package io.github.mooy1.infinitylib.commands;

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

    public static CommandManager setup(String command, String aliases, AbstractCommand... commands) {
        CommandManager manager = new CommandManager(Objects.requireNonNull(PluginUtils.getPlugin().getCommand(command)), aliases);
        for (AbstractCommand command1 : commands) {
            manager.addCommand(command1);
        }
        return manager;
    }
    
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    
    private final String aliases;
    private final String command;
    
    private CommandManager(PluginCommand command, String aliases) {
        this.aliases = ChatColors.color("&6Aliases: &e" + aliases);
        this.command = command.getName();
        
        command.setExecutor(this);
        command.setTabCompleter(this);
        
        addCommand(new Info());
        addCommand(new Help());
    }
    
    private void addCommand(AbstractCommand command) {
        this.commands.put(command.name, command);
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

    private final class Help extends AbstractCommand implements Listener {
        
        private Help() {
            super("help", "Displays this", false);
            PluginUtils.registerListener(this);
        }
    
        @Override
        public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
            sender.sendMessage("");
            sender.sendMessage(ChatColors.color("&7----------&b&l " + PluginUtils.getPlugin().getName() + " &7----------"));
            sender.sendMessage("");
            for (AbstractCommand command : CommandManager.this.commands.values()) {
                if (command.hasPerm(sender)) {
                    sender.sendMessage(ChatColors.color("&6/" + CommandManager.this.command + " " + command.name + " &e- " + command.description));
                }
            }
            sender.sendMessage("");
            sender.sendMessage(CommandManager.this.aliases);
            sender.sendMessage("");
        }

        @Override
        protected void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
            
        }

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent e) {
            if (e.getMessage().equalsIgnoreCase("/help " + CommandManager.this.command)) {
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

        @Override
        protected void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
            
        }

    }

}
