package io.github.mooy1.infinitylib.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;

class TestAddonLive {

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
    void testBadGithubStrings() {
        Assertions.assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.BAD_GITHUB_PATH));
    }

    @Test
    void testMissingAutoUpdateKey() {
        Assertions.assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.MISSING_KEY));
    }

    @Test
    void testSuperEnable() {
        Assertions.assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.CALL_SUPER));
        Assertions.assertDoesNotThrow(
                () -> manager.disablePlugin(MockAddon.instance()));
    }

    @Test
    void testErrorThrown() {
        Assertions.assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.THROW_EXCEPTION));
        Assertions.assertDoesNotThrow(
                () -> manager.disablePlugin(MockAddon.instance()));
    }

}
