package io.github.mooy1.infinitylib.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import io.github.mooy1.infinitylib.AbstractAddon;

public final class CommandUtils implements TabExecutor {
    
    public static void createSubCommands(AbstractAddon addon, String commandName, List<AbstractCommand> subCommands) {
        PluginCommand command = Objects.requireNonNull(addon.getCommand(commandName),
                () -> "No such command '" + commandName + "'!");
        subCommands = new ArrayList<>(subCommands);
        subCommands.add(new HelpCommand(subCommands, command));
        subCommands.add(new InfoCommand(addon));
        new CommandUtils(command, subCommands);
    }

    private final Map<String, AbstractCommand> commands = new HashMap<>();

    private CommandUtils(PluginCommand command, List<AbstractCommand> subCommands) {
        command.setExecutor(this);
        command.setTabCompleter(this);
        for (AbstractCommand abstractCommand : subCommands) {
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

}
