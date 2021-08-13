package io.github.mooy1.infinitylib.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

/**
 * The main command of an addon, which can hold multiple sub commands
 *
 * @author Mooy1
 */
@ParametersAreNonnullByDefault
public final class AddonCommand extends ParentCommand implements TabExecutor {

    public AddonCommand(String command) {
        this(Objects.requireNonNull(AbstractAddon.instance().getCommand(command),
                "No such command '" + command + "'! Add it it to your plugin.yml!"));
    }

    public AddonCommand(PluginCommand command) {
        super(command.getName(), command.getDescription());
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
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
