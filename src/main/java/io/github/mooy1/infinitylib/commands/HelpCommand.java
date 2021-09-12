package io.github.mooy1.infinitylib.commands;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import io.github.mooy1.infinitylib.common.Translations;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;

final class HelpCommand extends AbstractCommand implements Listener {

    private final List<AbstractCommand> subCommands;
    private final String command;
    private final String help;
    private final String header;
    private final String aliases;

    HelpCommand(ParentCommand command) {
        super("help", Translations.get("commands.help.description", "displays this"));
        this.command = command;
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        sender.sendMessage("");
        sender.sendMessage(this.header);
        sender.sendMessage("");
        for (AbstractCommand command : this.subCommands) {
            if (command.hasPerm(sender)) {
                sender.sendMessage(this.command + command.name + ChatColor.YELLOW + " - " + command.description);
            }
        }
        sender.sendMessage("");
        sender.sendMessage(this.aliases);
        sender.sendMessage("");
    }

    @Override
    public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase(this.help)) {
            onExecute(e.getPlayer(), new String[0]);
            e.setCancelled(true);
        }
    }

}
