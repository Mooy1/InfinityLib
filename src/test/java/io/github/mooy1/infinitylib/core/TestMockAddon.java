package io.github.mooy1.infinitylib.core;

import java.io.StringReader;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;

class TestMockAddon {

    private static PluginManagerMock manager;

    @BeforeAll
    public static void load() {
        manager = MockBukkit.mock().getPluginManager();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @BeforeEach
    void clear() {
        manager.clearPlugins();
    }

    @Test
    void testMissingAutoUpdateKey() {
        Assertions.assertThrows(RuntimeException.class, () -> loadMock(MockMissingKeyAddon.class));
    }

    @Test
    void testSuperEnable() {
        Assertions.assertThrows(IllegalStateException.class, () -> loadMock(MockCallSuperAddon.class));
        Assertions.assertThrows(IllegalStateException.class, () -> manager.disablePlugin(MockCallSuperAddon.instance()));
    }

    @Test
    void testErrorThrown() {
        Assertions.assertThrows(Error.class, () -> loadMock(MockCaughtErrorAddon.class));
        Plugin plugin = manager.getPlugin("MockAddon");
        Assertions.assertNotNull(plugin);
        Assertions.assertThrows(Error.class, () -> manager.enablePlugin(plugin));
        MockCaughtErrorAddon addon = MockCaughtErrorAddon.instance();
        Assertions.assertNotNull(addon);
        Assertions.assertThrows(Error.class, () -> manager.disablePlugin(addon));
    }

    @Test
    void testBadGithubStrings() {
        Assertions.assertThrows(RuntimeException.class, () -> loadMock(MockBadGithubAddon.class));
    }

    @Test
    void testNullInstance() {
        Assertions.assertThrows(NullPointerException.class, AbstractAddon::instance);
    }

    private static void loadMock(Class<? extends MockAddon> clazz) {
        String pluginYML = "name: MockAddon\n" +
                "version: Testing\n" +
                "main: " + clazz.getName() + '\n' +
                "commands:\n" +
                "  mockaddon:\n" +
                "    description: MockAddon Command\n";
        try {
            MockBukkit.loadWith(clazz, new PluginDescriptionFile(new StringReader(pluginYML)));
        } catch (InvalidDescriptionException e) {
            throw new RuntimeException(e);
        }
    }

}
