package io.github.mooy1.infinitylib.core;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import io.github.mooy1.otheraddon.MockOtherAddon;

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
    void testSharedInfinityLib() {
        PluginDescriptionFile desc = new PluginDescriptionFile("MockAddon", "", MockOtherAddon.class.getName());
        Assertions.assertThrows(RuntimeException.class, () -> MockBukkit.load(MockOtherAddon.class, desc, Environment.TESTING));
    }

    @Test
    void testBadGithubStrings() {
        Assertions.assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.BAD_GITHUB_PATH));
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
        Assertions.assertThrows(NullPointerException.class, MockAddon::instance);
        Plugin plugin = manager.getPlugin("MockAddon");
        Assertions.assertNotNull(plugin);
        Assertions.assertThrows(IllegalStateException.class,
                () -> manager.enablePlugin(plugin));
        Assertions.assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
        Assertions.assertThrows(IllegalStateException.class,
                () -> manager.disablePlugin(plugin));
    }

    @Test
    void testErrorThrown() {
        Assertions.assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.THROW_EXCEPTION));
        Assertions.assertThrows(NullPointerException.class, MockAddon::instance);
        Plugin plugin = manager.getPlugin("MockAddon");
        Assertions.assertNotNull(plugin);
        Assertions.assertThrows(RuntimeException.class,
                () -> manager.enablePlugin(plugin));
        Assertions.assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
        Assertions.assertThrows(RuntimeException.class,
                () -> manager.disablePlugin(plugin));
    }

}
