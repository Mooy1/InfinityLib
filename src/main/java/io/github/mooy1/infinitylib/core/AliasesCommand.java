package io.github.mooy1.infinitylib.core;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;


@ParametersAreNonnullByDefault
final class AliasesCommand extends SubCommand {

    private final String message;

    AliasesCommand(PluginCommand command) {
        super("aliases", "lists the available aliases for this command");
        this.message = command.getAliases().toString();
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        sender.sendMessage(this.message);
    }

    @Override
    protected void complete(CommandSender sender, String[] args, List<String> completions) {

    }

}
