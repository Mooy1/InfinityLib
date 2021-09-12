package io.github.mooy1.infinitylib.core;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import io.github.mooy1.otheraddon.MockOtherAddon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockOtherAddon.class, desc, Environment.TESTING));
    }

    @Test
    void testBadGithubStrings() {
        assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.BAD_GITHUB_PATH));
    }

    @Test
    void testMissingAutoUpdateKey() {
        assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.MISSING_KEY));
    }

    @Test
    void testSuperEnable() {
        assertThrows(IllegalStateException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.CALL_SUPER));
        assertThrows(NullPointerException.class, MockAddon::instance);
        Plugin plugin = manager.getPlugin("MockAddon");
        assertNotNull(plugin);
        assertThrows(IllegalStateException.class,
                () -> manager.enablePlugin(plugin));
        assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
        assertThrows(IllegalStateException.class,
                () -> manager.disablePlugin(plugin));
    }

    @Test
    void testErrorThrown() {
        assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.TESTING, MockAddonTest.THROW_EXCEPTION));
        assertThrows(NullPointerException.class, MockAddon::instance);
        Plugin plugin = manager.getPlugin("MockAddon");
        assertNotNull(plugin);
        assertThrows(RuntimeException.class,
                () -> manager.enablePlugin(plugin));
        assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
        assertThrows(RuntimeException.class,
                () -> manager.disablePlugin(plugin));
    }

}
