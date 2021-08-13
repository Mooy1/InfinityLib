package io.github.mooy1.infinitylib.core;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

@ParametersAreNonnullByDefault
class MockParentCommand extends ParentCommand {

    public MockParentCommand(String name) {
        super(name, name);
    }

    @Override
    protected void execute(CommandSender sender) {
        sender.sendMessage(name());
    }

}
