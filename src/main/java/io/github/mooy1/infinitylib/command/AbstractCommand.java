package io.github.mooy1.infinitylib.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

/**
 * A command for use in the {@link CommandManager}
 * 
 * @author Mooy1
 */
@Getter
public abstract class AbstractCommand {
    
    private final boolean op;
    private final String name;
    private final String description;
    
    public AbstractCommand(@Nonnull String name, @Nonnull String description, boolean op) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.description = description;
        this.op = op;
    }

    public abstract void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args);

    @Nonnull
    public abstract List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args);
    
}
