package io.github.mooy1.infinitylib.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

/**
 * The main command of an addon, which can hold multiple sub commands
 *
 * @author Mooy1
 */
final class AddonCommand extends ParentCommand implements TabExecutor {

    AddonCommand(PluginCommand command) {
        super(command.getName(), command.getDescription(),
                new InfoCommand((SlimefunAddon) command.getPlugin()),
                new AliasesCommand(command)
        );
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
        List<String> strings = new ArrayList<>();
        complete(sender, args, strings);
        List<String> returnList = new ArrayList<>();
        String arg = args[args.length - 1].toLowerCase(Locale.ROOT);
        for (String item : strings) {
            if (item.toLowerCase(Locale.ROOT).contains(arg)) {
                returnList.add(item);
                if (returnList.size() >= 64) {
                    break;
                }
            } else if (item.equalsIgnoreCase(arg)) {
                return Collections.emptyList();
            }
        }
        return returnList;
    }

    @Override
    String fullName() {
        return name();
    }

}
