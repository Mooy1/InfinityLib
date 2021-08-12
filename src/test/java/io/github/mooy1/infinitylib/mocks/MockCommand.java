package io.github.mooy1.infinitylib.mocks;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

import io.github.mooy1.infinitylib.core.SubCommand;

public final class MockCommand extends SubCommand {

    public MockCommand() {
        super("test", "test", true);
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
