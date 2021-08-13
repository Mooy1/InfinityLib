package io.github.mooy1.infinitylib.core;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

class MockSubCommand extends SubCommand {

    protected MockSubCommand(String name, String perm) {
        super(name, "test", perm);
    }

    protected MockSubCommand(String name, boolean op) {
        super(name, "test", op);
    }

    protected MockSubCommand(String name) {
        super(name, "test");
    }

    @Override
    protected void execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        sender.sendMessage("test");
    }

    @Override
    protected void complete(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
        tabs.add("test");
    }

}
