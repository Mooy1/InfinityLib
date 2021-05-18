package io.github.mooy1.infinitylib.commands;

import io.github.mooy1.infinitylib.AbstractAddon;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

final class ReloadCommand extends AbstractCommand {

    private final AbstractAddon addon;

    ReloadCommand(AbstractAddon addon) {
        super("reload", "Reloads the configuration of the addon", true);
        this.addon = addon;
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        addon.reloadConfig();
        sender.sendMessage("Reloaded config");
    }

    @Override
    public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
    }
}
