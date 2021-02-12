package io.github.mooy1.infinitylib.command;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.ChatColor;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Manages command stuff for your addon, register {@link AbstractCommand}s to this
 * 
 * @author Mooy1
 */
public final class CommandManager implements CommandExecutor, Listener, TabCompleter {

    private static final Set<AbstractCommand> COMMANDS = new HashSet<>();

    public static void setup(String command, String permission, String aliases, AbstractCommand... commands) {
        new CommandManager(Objects.requireNonNull(PluginUtils.getPlugin().getCommand(command)), permission, aliases);
        COMMANDS.addAll(Arrays.asList(commands));
    }
    
    private final String permission;
    private final String aliases;
    private final String help;
    private final String command;
    
    private CommandManager(PluginCommand command, String permission, String aliases) {
        this.permission = permission;
        this.aliases = aliases;
        this.command = command.getName();
        this.help = "/help " + command;
        PluginUtils.registerListener(this);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
    
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length > 0) {
            String arg = args[0];
            if (arg.equalsIgnoreCase("help")) {
                sendHelp(sender);
                return true;
            }
            if (arg.equalsIgnoreCase("info")) {
                sendInfo(sender);
                return true;
            }
            for (AbstractCommand libCommand : COMMANDS) {
                if (arg.equalsIgnoreCase(libCommand.getName())) {
                    if (!libCommand.isOp() || sender.hasPermission(this.permission)) {
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
        sender.sendMessage(ChatColors.color("&7----------&b&l " + PluginUtils.getPlugin().getName() + " &7----------"));
        sender.sendMessage("");
        for (AbstractCommand cmd : COMMANDS) {
            if (!cmd.isOp() || sender.hasPermission(this.permission)) {
                sender.sendMessage(ChatColors.color("&6/" + this.command + " " + cmd.getName() + " &e- " + cmd.getDescription()));
            }
        }
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&6Aliases: &e" + this.aliases));
        sender.sendMessage("");
    }
    
    public void sendInfo(@Nonnull CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&aSlimefun Version: " + Objects.requireNonNull(SlimefunPlugin.instance()).getPluginVersion()));
        sender.sendMessage(ChatColors.color("&bSlimefun Discord: &7Discord.gg/slimefun"));
        sender.sendMessage(ChatColors.color("&aAddon Version: " + PluginUtils.getAddon().getPluginVersion()));
        sender.sendMessage(ChatColors.color("&bAddon Community: &7Discord.gg/Will be added later"));
        sender.sendMessage(ChatColors.color("&aGithub: &7" + PluginUtils.getAddon().getBugTrackerURL()));
        sender.sendMessage("");
    }
    
    public void sendNoPerm(@Nonnull CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase(this.help)) {
            sendHelp(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
        if (args.length == 1) {

            List<String> subCommands = new ArrayList<>();
            subCommands.add("help");
            subCommands.add("info");
            for (AbstractCommand libCommand : COMMANDS) {
                if (!libCommand.isOp() || sender.hasPermission(this.permission)) {
                    subCommands.add(libCommand.getName());
                }
            }
            return createReturnList(subCommands, args[0]);

        } else if (args.length > 1) {
            for (AbstractCommand libCommand : COMMANDS) {
                if (args[0].equalsIgnoreCase(libCommand.getName())) {
                    return createReturnList(libCommand.onTab(sender, args), args[args.length - 1]);
                }
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
