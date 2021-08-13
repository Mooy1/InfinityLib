package io.github.mooy1.infinitylib.core;

import org.bukkit.command.CommandSender;

class MockParentCommand extends ParentCommand {

    public MockParentCommand(String name, String description) {
        super(name, description);
    }

    @Override
    protected void execute(CommandSender sender) {
        sender.sendMessage("test");
    }

}
