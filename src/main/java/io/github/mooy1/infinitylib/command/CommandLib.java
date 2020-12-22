package io.github.mooy1.infinitylib.command;

import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class CommandLib implements CommandExecutor, Listener, TabCompleter {

    private static final Set<LibCommand> commands = new HashSet<>();
    
    private static String permission;
    private static String aliases;
    private static JavaPlugin plugin;
    private static String help;
    
    public void setup(JavaPlugin plugin, String command, String permission, String aliases) {
        CommandLib.permission = permission;
        CommandLib.aliases = aliases;
        CommandLib.plugin = plugin;
        CommandLib.help = "/help " + command; 
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Objects.requireNonNull(plugin.getCommand(command)).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(command)).setTabCompleter(this);
    }
    
    public static void addCommand(@Nonnull LibCommand command) {
        commands.add(command);
    }
    
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
                return true;
            }
            for (LibCommand libCommand : commands) {
                if (args[0].equalsIgnoreCase(libCommand.getName())) {
                    if (!libCommand.isOp() || sender.hasPermission(permission)) {
                        libCommand.onExecute(sender, args);
                    } else {
                        sendNoPerm(sender);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void sendHelp(@Nonnull CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&7----------&b&l " + plugin.getName() + " &7----------"));
        sender.sendMessage("");
        
        for (LibCommand cmd : commands) {
            if (!cmd.isOp() || sender.hasPermission(permission)) {
                sender.sendMessage(ChatColors.color("&6/ie " + cmd.getName() + " &e- " + cmd.getDescription()));
            }
        }
        
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Aliases: &e" + aliases));
        sender.sendMessage("");
    }

    public void sendNoPerm(@Nonnull CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase(help)) {
            sendHelp(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
        if (args.length == 1) {

            List<String> subCommands = new ArrayList<>();
            subCommands.add("help");
            for (LibCommand libCommand : commands) {
                if (!libCommand.isOp() || sender.hasPermission(permission)) {
                    subCommands.add(command.getName());
                }
            }
            return createReturnList(subCommands, args[0]);

        } else if (args.length > 1) {
            for (LibCommand libCommand : commands) {
                if (args[0].equalsIgnoreCase(libCommand.getName())) {
                    return createReturnList(libCommand.onTab(sender, args), args[args.length - 1]);
                }
            }
        }

        return new ArrayList<>();
    }

    @Nonnull
    private List<String> createReturnList(@Nonnull List<String> list, @Nonnull String string) {
        if (string.length() == 0) {
            return list;
        }

        String input = string.toLowerCase(Locale.ROOT);
        List<String> returnList = new LinkedList<>();

        for (String item : list) {
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

}
