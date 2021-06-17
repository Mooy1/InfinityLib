package io.github.mooy1.infinitylib.commands;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.command.CommandMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.mooy1.infinitylib.mocks.MockAddon;
import io.github.mooy1.infinitylib.mocks.MockCommand;

class TestCommandUtils {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);

        CommandUtils.addSubCommands(addon, "mockcommand", Collections.singletonList(new MockCommand()));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testInfoAndHelp() {
        // TODO implement
    }

    @Test
    void testCommandExecute() {
        server.execute("mockcommand", server.addPlayer(), "test").assertFailed();
        server.executeConsole("mockcommand", "test").assertResponse("test");
    }

    @Test
    void testTabComplete() {
        CommandMap map = server.getCommandMap();
        Assertions.assertFalse(map.tabComplete(server.addPlayer(), "mockcommand ").contains("test"));
        Assertions.assertTrue(map.tabComplete(server.getConsoleSender(), "mockcommand ").contains("test"));
    }

    @Test
    void testNoSuchCommand() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> CommandUtils.addSubCommands(addon, "fail", new ArrayList<>()));
    }

}
