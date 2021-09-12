package io.github.mooy1.infinitylib.commands;

import java.util.List;
import java.util.Objects;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.core.MockAddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertThrows(NullPointerException.class, () -> new AddonCommand("fail"));
    }

    @Test
    void testAddParentToChildCommand() {
        ParentCommand child = new ParentCommand("child", "");
        ParentCommand parent = new ParentCommand("parent", "").addSub(child);
        assertThrows(IllegalArgumentException.class, () -> child.addSub(parent));
    }

    @Test
    void testAddSubCommandToSelf() {
        ParentCommand parent = new ParentCommand("parent", "");
        assertThrows(IllegalArgumentException.class, () -> parent.addSub(parent));
    }

    @Test
    void testDuplicateSubCommand() {
        ParentCommand child = new ParentCommand("child", "");
        ParentCommand parent = new ParentCommand("parent", "").addSub(child);
        assertThrows(IllegalArgumentException.class, () -> parent.addSub(child));
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
    void testDefaultCommands() {
        PlayerMock p = server.addPlayer();
        assertTrue(getResponse(p, command, "info").contains("Info"));
        assertTrue(getResponse(p, command, "aliases").contains("Aliases"));
    }

    @Test
    void testOpSubCommand() {
        String op = "op";
        addonCommand.addSub(new MockSubCommand("op", true));

        PlayerMock p = server.addPlayer();
        assertFalse(getResponse(p, command).contains(op));
        assertNoResponse(p, op);
        assertNoCompletion(p, op);
        assertNoCompletion(p, op, op);

        p.setOp(true);
        assertTrue(getResponse(p, command).contains(op));
        assertResponse(p, op);
        assertCompletion(p, op);
        assertCompletion(p, op, op);
    }

    @Test
    void testPermissionSubCommand() {
        String perm = "perm";
        addonCommand.addSub(new MockSubCommand(perm, perm));

        PlayerMock p = server.addPlayer();
        assertFalse(getResponse(p, command).contains(perm));
        assertNoResponse(p, perm);
        assertNoCompletion(p, perm);
        assertNoCompletion(p, perm, perm);

        p.addAttachment(AbstractAddon.instance()).setPermission(perm, true);
        assertTrue(getResponse(p, command).contains(perm));
        assertResponse(p, perm);
        assertCompletion(p, perm);
        assertCompletion(p, perm, perm);
    }

    @Test
    void testHelp() {
        String help = "help";
        PlayerMock p = server.addPlayer();

        String help1 = getResponse(p, command);
        String help2 = getResponse(p, command, help);

        assertTrue(help1.contains("Help"));
        assertEquals(help1, help2);
    }

    private static String getResponse(PlayerMock p, String... args) {
        p.performCommand(String.join(" ", args));
        String temp;
        StringBuilder response = new StringBuilder();
        while ((temp = p.nextMessage()) != null) {
            response.append(temp);
        }
        return response.toString();
    }

    private static void assertResponse(CommandSender sender, String... args) {
        server.execute(command, sender, args).assertResponse(args[args.length - 1]);
    }

    private static void assertNoResponse(CommandSender sender, String... args) {
        server.execute(command, sender, args).assertNoResponse();
    }

    private static void assertCompletion(CommandSender sender, String completion, String... args) {
        assertTrue(completions(sender, args).contains(completion));
    }

    private static void assertNoCompletion(CommandSender sender, String completion, String... args) {
        assertFalse(completions(sender, args).contains(completion));
    }

    private static List<String> completions(CommandSender sender, String... args) {
        StringBuilder line = new StringBuilder(command).append(' ');
        for (String arg : args) {
            line.append(arg).append(' ');
        }
        return Objects.requireNonNull(map.tabComplete(sender, line.toString()));
    }

}
