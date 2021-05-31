package io.github.mooy1.infinitylib.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.mooy1.infinitylib.commands.AbstractCommand;
import io.github.mooy1.infinitylib.commands.CommandUtils;

class TestCommands {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);

        List<AbstractCommand> commands = Collections.singletonList(new AbstractCommand("test", "test", true) {

            @Override
            public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
                sender.sendMessage("test");
            }

            @Override
            public void onTab(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
                tabs.add("test");
            }

        });

        CommandUtils.addSubCommands(addon, "test", commands);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testCommands() {
        server.execute("test", server.addPlayer(), "test").assertFailed();
        server.executeConsole("test", "test").assertResponse("test");
    }

    @Test
    void testTabComplete() {
        CommandMap map = server.getCommandMap();
        Assertions.assertFalse(map.tabComplete(server.addPlayer(), "test ").contains("test"));
        Assertions.assertTrue(map.tabComplete(server.getConsoleSender(), "test ").contains("test"));
    }

    @Test
    void testNoSuchCommand() {
        Assertions.assertThrows(NullPointerException.class,
                () -> CommandUtils.addSubCommands(addon, "fail", new ArrayList<>()));
    }

}
