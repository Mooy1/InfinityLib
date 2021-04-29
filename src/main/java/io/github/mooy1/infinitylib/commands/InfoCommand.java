package io.github.mooy1.infinitylib.commands;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;

final class InfoCommand extends AbstractCommand {

    private final String[] message;

    InfoCommand(AbstractAddon addon) {
        super("info", "Gives addon version information", false);
        this.message = new String[] {
                "",
                ChatColors.color("&b" + addon.getName() + " Info"),
                ChatColors.color("&bSlimefun Version: &7" + Objects.requireNonNull(SlimefunPlugin.instance()).getPluginVersion()),
                ChatColors.color("&bSlimefun Discord: &7Discord.gg/slimefun"),
                ChatColors.color("&bAddon Version: &7" + addon.getPluginVersion()),
                ChatColors.color("&bAddon Community: &7Discord.gg/SqD3gg5SAU"),
                ChatColors.color("&bGithub: &7" + addon.getBugTrackerURL()),
                ""
        };
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        sender.sendMessage(this.message);
    }

    @Override
    public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {

    }

}
