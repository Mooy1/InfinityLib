package io.github.mooy1.infinitylib.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

@ParametersAreNonnullByDefault
public class ParentCommand extends SubCommand {

    private final Map<String, SubCommand> commands = new HashMap<>();
    private final SubCommand helpCommand = new HelpCommand(this);

    public ParentCommand(String name, String description, String perm) {
        super(name, description, perm);
        addSub(helpCommand);
    }

    public ParentCommand(String name, String description, boolean op) {
        super(name, description, op);
        addSub(helpCommand);
    }

    public ParentCommand(String name, String description) {
        super(name, description);
        addSub(helpCommand);
    }

    @Nonnull
    public final ParentCommand addSub(SubCommand command) {
        if (command == this) {
            throw new IllegalArgumentException("'" + command.name() + "' cannot be added to itself!");
        }
        if (command == parent) {
            throw new IllegalArgumentException("Parent command '" + command.name() + "' cannot be added to child " + name());
        }
        commands.compute(command.name(), (name, cmd) -> {
            if (cmd != null) {
                throw new IllegalArgumentException("Duplicate command '" + command.name() + "' cannot be added to " + name());
            }
            command.parent = this;
            return command;
        });
        return this;
    }

    @Override
    protected final void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            execute(sender);
        }
        else {
            SubCommand command = commands.get(args[0]);
            if (command != null && command.canUse(sender)) {
                command.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }

    /**
     * This is called when no sub command is used
     */
    protected void execute(CommandSender sender) {
        helpCommand.execute(sender, new String[0]);
    }

    @Override
    protected final void complete(CommandSender sender, String[] args, List<String> completions) {
        SubCommand command = commands.get(args[0]);
        if (command != null && command.canUse(sender)) {
            command.complete(sender, Arrays.copyOfRange(args, 1, args.length), completions);
        }
        else {
            for (SubCommand sub : commands.values()) {
                if (sub.canUse(sender)) {
                    completions.add(sub.name());
                }
            }
        }
    }

    @Nonnull
    final Set<SubCommand> available(CommandSender sender) {
        Set<SubCommand> set = new HashSet<>();
        for (SubCommand command : commands.values()) {
            if (command.canUse(sender)) {
                set.add(command);
            }
        }
        return set;
    }

}