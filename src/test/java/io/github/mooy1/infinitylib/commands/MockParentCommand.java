package io.github.mooy1.infinitylib.commands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

@ParametersAreNonnullByDefault
public class MockParentCommand extends ParentCommand {

    public MockParentCommand(String name) {
        super(name, name);
    }

    @Override
    protected void execute(CommandSender sender) {
        sender.sendMessage(name());
    }

}
