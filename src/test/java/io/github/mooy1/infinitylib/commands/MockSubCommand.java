package io.github.mooy1.infinitylib.commands;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

@ParametersAreNonnullByDefault
public class MockSubCommand extends SubCommand {

    public MockSubCommand(String name, String perm) {
        super(name, name, perm);
    }

    public MockSubCommand(String name, boolean op) {
        super(name, name, op);
    }

    public MockSubCommand(String name) {
        super(name, name);
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        sender.sendMessage(name());
    }

    @Override
    protected void complete(CommandSender sender, String[] args, List<String> tabs) {
        tabs.add(name());
    }

}
