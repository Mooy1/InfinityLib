package io.github.mooy1.infinitylib.command;

import io.github.mooy1.infinitylib.InfinityAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
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

public final class CommandManager implements TabExecutor {
    
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    private final InfinityAddon addon;
    private final PluginCommand command;
    
    public CommandManager(InfinityAddon addon, PluginCommand command, AbstractCommand... commands) {
        this.addon = addon;
        this.command = command;
        for (AbstractCommand command1 : commands) {
            this.commands.put(command1.name, command1);
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
    
        private HelpCommand(InfinityAddon addon) {
            super("help", "Displays this", false);
            addon.registerListener(this);
        }
    
        @Override
        public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
            sender.sendMessage("");
            sender.sendMessage(ChatColors.color("&7----------&b&l " + CommandManager.this.addon.getName() + " &7----------"));
            sender.sendMessage("");
            for (AbstractCommand command : CommandManager.this.commands.values()) {
                if (command.hasPerm(sender)) {
                    sender.sendMessage(ChatColors.color("&6/" + CommandManager.this.command.getName() + " " + command.name + " &e- " + command.description));
                }
            }
            sender.sendMessage("");
            sender.sendMessage("&6Aliases: &e" + CommandManager.this.command.getAliases());
            sender.sendMessage("");
        }
    
        @Override
        public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
    
        }
    
        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent e) {
            if (e.getMessage().equalsIgnoreCase("/help " + CommandManager.this.command.getName())) {
                onExecute(e.getPlayer(), new String[0]);
                e.setCancelled(true);
            }
        }
    
    }

    private final class InfoCommand extends AbstractCommand {
    
        private InfoCommand() {
            super("info", "Gives version information", false);
        }
    
        @Override
        public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
            sender.sendMessage(new String[] {
                    "",
                    ChatColors.color("&aSlimefun Version: " + Objects.requireNonNull(SlimefunPlugin.instance()).getPluginVersion()),
                    ChatColors.color("&bSlimefun Discord: &7Discord.gg/slimefun"),
                    ChatColors.color("&aAddon Version: " + CommandManager.this.addon.getPluginVersion()),
                    ChatColors.color("&bAddon Community: &7Discord.gg/SqD3gg5SAU"),
                    ChatColors.color("&aGithub: &7" + CommandManager.this.addon.getBugTrackerURL()),
                    ""
            });
        }
    
        @Override
        public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
            
        }
    
    }

}
