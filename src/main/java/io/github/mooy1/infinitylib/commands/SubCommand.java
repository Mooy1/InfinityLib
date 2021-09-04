package io.github.mooy1.infinitylib.commands;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

/**
 * A command
 *
 * @author Mooy1
 */
@ParametersAreNonnullByDefault
public abstract class SubCommand {

    private final Predicate<CommandSender> permission;
    private final String description;
    private final String name;
    private String fullName;
    ParentCommand parent;

    protected SubCommand(String name, String description, String perm) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.description = description;
        this.permission = sender -> sender.hasPermission(perm);
    }

    protected SubCommand(String name, String description, boolean op) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.description = description;
        this.permission = op ? CommandSender::isOp : sender -> true;
    }

    protected SubCommand(String name, String description) {
        this(name, description, false);
    }

    protected abstract void execute(CommandSender sender, String[] args);

    protected abstract void complete(CommandSender sender, String[] args, List<String> completions);

    public final boolean canUse(CommandSender sender) {
        return permission.test(sender);
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }

    String fullName() {
        if (fullName == null) {
            fullName = parent.fullName() + " " + name();
        }
        return fullName;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SubCommand && ((SubCommand) obj).name.equals(name);
    }

}
