package io.github.mooy1.infinitylib.commands;

import java.util.List;
import java.util.Objects;

import org.bukkit.command.CommandSender;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;

final class InfoCommand extends SubCommand {

    private final String[] message;

    InfoCommand(SlimefunAddon addon) {
        super("info", "Gives addon and machines version and discord links");
        message = new String[] {
                "",
                ChatColors.color("&b" + addon.getName() + " Info"),
                ChatColors.color("&bSlimefun Version: &7" + Objects.requireNonNull(Slimefun.instance()).getPluginVersion()),
                ChatColors.color("&bSlimefun Discord: &7Discord.gg/machines"),
                ChatColors.color("&bAddon Version: &7" + addon.getPluginVersion()),
                ChatColors.color("&bAddon Community: &7Discord.gg/SqD3gg5SAU"),
                ChatColors.color("&bGithub: &7" + addon.getBugTrackerURL()),
                ""
        };
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        sender.sendMessage(message);
    }

    @Override
    protected void complete(CommandSender sender, String[] args, List<String> tabs) {

    }

}
