package io.github.mooy1.infinitylib.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;

class TestAddonTesting {

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
        Assertions.assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.MISSING_KEY));
    }

    @Test
    void testSuperEnable() {
        Assertions.assertThrows(IllegalStateException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.CALL_SUPER));
        Assertions.assertThrows(IllegalStateException.class,
                () -> manager.enablePlugin(MockAddon.instance()));
        Assertions.assertThrows(IllegalStateException.class,
                () -> manager.disablePlugin(MockAddon.instance()));
    }

    @Test
    void testErrorThrown() {
        Assertions.assertThrows(Error.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.THROW_ERROR));
        Assertions.assertThrows(Error.class,
                () -> manager.enablePlugin(MockAddon.instance()));
        Assertions.assertThrows(Error.class,
                () -> manager.disablePlugin(MockAddon.instance()));
    }

    @Test
    void testBadGithubStrings() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.BAD_GITHUB_PATH));
    }

}
