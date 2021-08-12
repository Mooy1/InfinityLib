package io.github.mooy1.infinitylib.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

@ParametersAreNonnullByDefault
public class ParentCommand extends SubCommand {

    private final Map<String, SubCommand> commands = new HashMap<>();
    private final SubCommand helpCommand = new HelpCommand(this);

    public ParentCommand(String name, String description, String perm, SubCommand... subCommands) {
        super(name, description, perm);
        initialize(subCommands);
    }

    public ParentCommand(String name, String description, boolean op, SubCommand... subCommands) {
        super(name, description, op);
        initialize(subCommands);
    }

    public ParentCommand(String name, String description, SubCommand... subCommands) {
        super(name, description);
        initialize(subCommands);
    }

    void addCommand(SubCommand command) {
        this.commands.compute(command.name(), (name, cmd) -> {
            if (cmd != null) {
                throw new IllegalStateException("Duplicate command '" + command.name() + "' registered too" + ParentCommand.this.name());
            }
            return command;
        });
    }

    private void initialize(SubCommand[] subCommands) {
        addCommand(this.helpCommand);
        for (SubCommand command : subCommands) {
            addCommand(command);
        }
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
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
    protected final void execute(CommandSender sender) {
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

    protected final Set<SubCommand> available(CommandSender sender) {
        Set<SubCommand> set = new HashSet<>();
        for (SubCommand command : this.commands.values()) {
            if (command.canUse(sender)) {
                set.add(command);
            }
        }
        return set;
    }

}