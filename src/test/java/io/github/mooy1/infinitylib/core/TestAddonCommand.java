package io.github.mooy1.infinitylib.core;

import java.util.Objects;

import org.bukkit.command.CommandMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

class TestAddonCommand {

    private static ServerMock server;
    private static AddonCommand command;
    private static CommandMap map;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        map = server.getCommandMap();
        MockBukkit.load(MockAddon.class);
        command = new AddonCommand("test");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testNoSuchCommand() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AddonCommand("fail"));
    }

    @Test
    void testHelp() {
        ConsoleCommandSenderMock sender = (ConsoleCommandSenderMock) server.getConsoleSender();
        server.executeConsole("test").assertSucceeded();
        String help1 = sender.nextMessage();
        server.executeConsole("test", "help").assertSucceeded();
        String help2 = sender.nextMessage();
        Assertions.assertEquals(help1, help2);
    }

    @Test
    void testSubCommand() {
        server.executeConsole("test", "test").assertResponse("test");
        Assertions.assertTrue(Objects.requireNonNull(
                map.tabComplete(server.getConsoleSender(), "test ")).contains("test"));
    }

    @Test
    void testNestedParentCommand() {
        command.addSub(new MockParentCommand("custom", "").addSub(new MockSubCommand("test", "")));
        server.executeConsole("test", "custom").assertResponse("test");
        server.executeConsole("test", "custom", "test").assertResponse("test");
        Assertions.assertTrue(Objects.requireNonNull(
                map.tabComplete(server.getConsoleSender(), "test custom ")).contains("test"));
    }

    @Test
    void testAddParentToChildCommand() {
        ParentCommand parent = new ParentCommand("parent", "");
        ParentCommand child = new ParentCommand("child", "");
        parent.addSub(child);
        Assertions.assertThrows(IllegalArgumentException.class, () -> child.addSub(parent));
    }

    @Test
    void testAddSubCommandToSelf() {
        ParentCommand parent = new ParentCommand("parent", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addSub(parent));
    }

    @Test
    void testDuplicateSubCommand() {
        ParentCommand parent = new ParentCommand("parent", "");
        ParentCommand child = new ParentCommand("child", "");
        parent.addSub(child);
        Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addSub(child));
    }

    @Test
    void testOpSubCommand() {
        command.addSub(new MockSubCommand("op"));
        PlayerMock p = server.addPlayer();
        Assertions.assertFalse(p.performCommand("test op"));
        Assertions.assertFalse(Objects.requireNonNull(map.tabComplete(p, "test ")).contains("op"));
        p.setOp(true);
        Assertions.assertTrue(p.performCommand("test op"));
        Assertions.assertTrue(Objects.requireNonNull(map.tabComplete(p, "test ")).contains("op"));
        // TODO add help checks
    }

    @Test
    void testPermissionSubCommand() {
        command.addSub(new MockSubCommand("perm", "test"));
        PlayerMock p = server.addPlayer();
        Assertions.assertFalse(p.performCommand("test perm"));
        Assertions.assertFalse(Objects.requireNonNull(map.tabComplete(p, "test ")).contains("perm"));
        p.addAttachment(AbstractAddon.instance()).setPermission("test", true);
        Assertions.assertTrue(p.performCommand("test perm"));
        Assertions.assertTrue(Objects.requireNonNull(map.tabComplete(p, "test ")).contains("perm"));
        // TODO add help checks
    }

}
