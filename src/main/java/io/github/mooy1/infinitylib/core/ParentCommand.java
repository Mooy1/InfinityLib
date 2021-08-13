package io.github.mooy1.infinitylib.core;

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
    }

    public ParentCommand(String name, String description, boolean op) {
        super(name, description, op);
    }

    public ParentCommand(String name, String description) {
        super(name, description);
    }

    @Nonnull
    ParentCommand addSub(SubCommand command) {
        if (command == this) {
            throw new IllegalArgumentException("'" + command.name() + "' cannot be added to itself!");
        }
        if (command == this.parent) {
            throw new IllegalArgumentException("Parent command '" + command.name() + "' cannot be added to child " + this.name());
        }
        this.commands.compute(command.name(), (name, cmd) -> {
            if (cmd != null) {
                throw new IllegalArgumentException("Duplicate command '" + command.name() + "' cannot be added to " + this.name());
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
        } else {
            SubCommand command = this.commands.get(args[0]);
            if (command != null && command.canUse(sender)) {
                command.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }

    /**
     * This is called when no sub command is used
     */
    protected void execute(CommandSender sender) {
        this.helpCommand.execute(sender, new String[0]);
    }

    @Override
    protected final void complete(CommandSender sender, String[] args, List<String> completions) {
        if (args.length == 0) {
            for (SubCommand command : this.commands.values()) {
                if (command.canUse(sender)) {
                    completions.add(command.name());
                }
            }
        } else {
            SubCommand command = this.commands.get(args[0]);
            if (command != null && command.canUse(sender)) {
                command.complete(sender, Arrays.copyOfRange(args, 1, args.length), completions);
            }
        }
    }

    final Set<SubCommand> available(CommandSender sender) {
        Set<SubCommand> set = new HashSet<>();
        for (SubCommand command : this.commands.values()) {
            if (command.canUse(sender)) {
                set.add(command);
            }
        }
        return set;
    }

}