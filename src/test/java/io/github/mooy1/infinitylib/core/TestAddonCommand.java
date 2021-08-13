package io.github.mooy1.infinitylib.core;

import java.util.List;
import java.util.Objects;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

class TestAddonCommand {

    private static AddonCommand addonCommand;
    private static CommandSender console;
    private static ServerMock server;
    private static String command;
    private static CommandMap map;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        map = server.getCommandMap();
        MockBukkit.load(MockAddon.class);
        command = "test";
        addonCommand = new AddonCommand(command);
        console = server.getConsoleSender();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testNoSuchCommand() {
        Assertions.assertThrows(NullPointerException.class, () -> new AddonCommand("fail"));
    }

    @Test
    void testAddParentToChildCommand() {
        ParentCommand child = new ParentCommand("child", "");
        ParentCommand parent = new ParentCommand("parent", "").addSub(child);
        Assertions.assertThrows(IllegalArgumentException.class, () -> child.addSub(parent));
    }

    @Test
    void testAddSubCommandToSelf() {
        ParentCommand parent = new ParentCommand("parent", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addSub(parent));
    }

    @Test
    void testDuplicateSubCommand() {
        ParentCommand child = new ParentCommand("child", "");
        ParentCommand parent = new ParentCommand("parent", "").addSub(child);
        Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addSub(child));
    }

    @Test
    void testSubCommand() {
        String sub = "sub";
        addonCommand.addSub(new MockSubCommand(sub));
        assertResponse(console, sub);
        assertCompletion(console, sub);
        assertCompletion(console, sub, sub);
    }

    @Test
    void testParentCommand() {
        String parent = "parent";
        String child = "child";
        addonCommand.addSub(new MockParentCommand(parent).addSub(new MockSubCommand(child)));
        assertResponse(console, parent);
        assertResponse(console, parent, child);
        assertCompletion(console, child, parent);
    }

    @Test
    void testOpSubCommand() {
        String op = "op";
        addonCommand.addSub(new MockSubCommand("op", true));

        PlayerMock p = server.addPlayer();
        assertNoResponse(p, op);
        assertNoCompletion(p, op);
        assertNoCompletion(p, op, op);

        p.setOp(true);
        assertResponse(p, op);
        assertCompletion(p, op);
        assertCompletion(p, op, op);

        // TODO add help checks
    }

    @Test
    void testPermissionSubCommand() {
        String perm = "perm";
        addonCommand.addSub(new MockSubCommand(perm, perm));

        PlayerMock p = server.addPlayer();
        assertNoResponse(p, perm);
        assertNoCompletion(p, perm);
        assertNoCompletion(p, perm, perm);

        p.addAttachment(AbstractAddon.instance()).setPermission(perm, true);
        assertResponse(p, perm);
        assertCompletion(p, perm);
        assertCompletion(p, perm, perm);

        // TODO add help checks
    }

    @Test
    void testHelp() {
        ConsoleCommandSenderMock sender = (ConsoleCommandSenderMock) console;
        String temp;

        // TODO move help string to method

        server.executeConsole(command).assertSucceeded();
        StringBuilder help1 = new StringBuilder();
        while ((temp = sender.nextMessage()) != null) {
            help1.append(temp);
        }

        server.executeConsole(command, "help").assertSucceeded();
        StringBuilder help2 = new StringBuilder();
        while ((temp = sender.nextMessage()) != null) {
            help2.append(temp);
        }

        Assertions.assertEquals(help1.toString(), help2.toString());
    }

    private static void assertResponse(CommandSender sender, String... args) {
        server.execute(command, sender, args).assertResponse(args[args.length - 1]);
    }

    private static void assertNoResponse(CommandSender sender, String... args) {
        server.execute(command, sender, args).assertNoResponse();
    }

    private static void assertCompletion(CommandSender sender, String completion, String... args) {
        Assertions.assertTrue(completions(sender, args).contains(completion));
    }

    private static void assertNoCompletion(CommandSender sender, String completion, String... args) {
        Assertions.assertFalse(completions(sender, args).contains(completion));
    }

    private static List<String> completions(CommandSender sender, String... args) {
        StringBuilder line = new StringBuilder(command).append(' ');
        for (String arg : args) {
            line.append(arg).append(' ');
        }
        return Objects.requireNonNull(map.tabComplete(sender, line.toString()));
    }

}
