package io.github.mooy1.infinitylib.mocks;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

import io.github.mooy1.infinitylib.commands.AbstractCommand;

public final class MockCommand extends AbstractCommand {

    public MockCommand() {
        super("test", "test", true);
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        sender.sendMessage("test");
    }

    @Override
    public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
        tabs.add("test");
    }

}
