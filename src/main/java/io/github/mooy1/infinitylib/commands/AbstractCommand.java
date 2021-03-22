package io.github.mooy1.infinitylib.commands;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

/**
 * A command for use in the {@link CommandManager}
 * 
 * @author Mooy1
 */
public abstract class AbstractCommand {
    
    private final String perm;
    private final boolean op;
    final String name;
    final String description;
    
    public AbstractCommand(@Nonnull String name, @Nonnull String description, @Nonnull String perm) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.description = description;
        this.perm = perm;
        this.op = false;
    }

    public AbstractCommand(@Nonnull String name, @Nonnull String description, boolean op) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.description = description;
        this.perm = null;
        this.op = op;
    }

    protected abstract void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args);

    protected abstract void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs);
    
    boolean hasPerm(CommandSender sender) {
        return this.op ? sender.isOp() : this.perm == null || sender.hasPermission(this.perm);
    }
    
}
