package io.github.mooy1.infinitylib.core;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import io.github.mooy1.infinitylib.utils.Events;

final class HelpCommand extends SubCommand implements Listener {

    private final ParentCommand parentCommand;

    HelpCommand(ParentCommand parentCommand) {
        super("help", "Displays this");
        this.parentCommand = parentCommand;

        Events.register(PlayerCommandPreprocessEvent.class, EventPriority.MONITOR, true, e -> {
            if (e.getMessage().equalsIgnoreCase("/help " + this.parentCommand.fullName())) {
                execute(e.getPlayer(), new String[0]);
                e.setCancelled(true);
            }
        });
    }

    @Override
    public void execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        sender.sendMessage("");
        sender.sendMessage("&7----------&b " + this.parentCommand.name() + " Help &7----------");
        sender.sendMessage("");
        for (SubCommand sub : this.parentCommand.available(sender)) {
            sender.sendMessage("/" + sub.fullName() + ChatColor.YELLOW + " - " + sub.description());
        }
        sender.sendMessage("");
    }

    @Override
    public void complete(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {

    }

}
