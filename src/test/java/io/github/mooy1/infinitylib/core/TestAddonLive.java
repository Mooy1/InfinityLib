package io.github.mooy1.infinitylib.core;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import io.github.mooy1.otheraddon.MockOtherAddon;

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
    void testNullInstance() {
        Assertions.assertThrows(NullPointerException.class, AbstractAddon::instance);
    }

    @Test
    void testNoCommand() {
        Assertions.assertDoesNotThrow(() -> MockBukkit.loadWith(MockAddon.class,
                new PluginDescriptionFile("MockAddon", "", MockAddon.class.getName())));
        Assertions.assertThrows(NullPointerException.class, () -> MockAddon.instance().getAddonCommand());
    }

    @Test
    void testSharedInfinityLib() {
        PluginDescriptionFile desc = new PluginDescriptionFile("MockAddon", "", MockOtherAddon.class.getName());
        Assertions.assertThrows(RuntimeException.class, () -> MockBukkit.load(MockOtherAddon.class, desc, Environment.LIBRARY_TESTING));
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
