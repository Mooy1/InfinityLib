package io.github.mooy1.infinitylib.tests;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.command.CommandMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.mooy1.infinitylib.commands.CommandUtils;
import io.github.mooy1.infinitylib.mocks.MockAddon;
import io.github.mooy1.infinitylib.mocks.MockCommand;

class TestCommands {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);

        CommandUtils.addSubCommands(addon, "mockaddon", Collections.singletonList(new MockCommand()));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testCommands() {
        server.execute("mockaddon", server.addPlayer(), "test").assertFailed();
        server.executeConsole("mockaddon", "test").assertResponse("test");
    }

    @Test
    void testTabComplete() {
        CommandMap map = server.getCommandMap();
        Assertions.assertFalse(map.tabComplete(server.addPlayer(), "mockaddon ").contains("test"));
        Assertions.assertTrue(map.tabComplete(server.getConsoleSender(), "mockaddon ").contains("test"));
    }

    @Test
    void testNoSuchCommand() {
        Assertions.assertThrows(NullPointerException.class,
                () -> CommandUtils.addSubCommands(addon, "fail", new ArrayList<>()));
    }

}
