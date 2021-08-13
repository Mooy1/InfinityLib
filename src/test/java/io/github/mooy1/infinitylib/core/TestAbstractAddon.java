package io.github.mooy1.infinitylib.core;

import java.io.StringReader;
import java.util.logging.Level;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.ThrowingSupplier;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestAbstractAddon {

    private static PluginManager manager;
    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        manager = server.getPluginManager();
        addon = MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testInstance() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
        manager.clearPlugins();
        Assertions.assertThrows(NullPointerException.class, MockAddon::instance);
    }

    @Test
    void testOfficialBuild() {
        Assertions.assertFalse(MockAddon.isOfficialBuild());
    }

    @Test
    void testNotTesting() {
        Assertions.assertFalse(MockAddon.isNotTesting());
    }

    @Test
    void testNotNullConfig() {
        Assertions.assertNotNull(MockAddon.config());
    }

    @Test
    void testMakeKey() {
        Assertions.assertNotNull(MockAddon.makeKey("test"));
    }

    @Test
    void testAutoUpdatedDisabled() {
        Assertions.assertFalse(addon.autoUpdatesEnabled());
    }

    @Test
    void testBugTrackerURL() {
        Assertions.assertEquals("https://github.com/Mooy1/InfinityLib/issues", addon.getBugTrackerURL());
    }

    @Test
    void testLog() {
        Assertions.assertDoesNotThrow(() -> MockAddon.log(Level.INFO, "test"));
    }


    @Test
    void testGlobalTick() {
        server.getScheduler().performOneTick();
        Assertions.assertEquals(1, MockAddon.tickCount());
    }

    @Test
    void testCommand() {
        Assertions.assertNotNull(MockAddon.instance().getCommand());
        server.executeConsole("mockaddon").assertSucceeded();
        server.executeConsole("mockaddon", "info").assertSucceeded();
        server.executeConsole("mockaddon", "aliases").assertSucceeded();
    }

    @Test
    void testSharedInfinityLib() {
        Assertions.assertThrows(IllegalStateException.class, () -> MockBukkit.load(MockAddon.class));
        Assertions.assertSame(addon, MockAddon.instance());
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void testMissingAutoUpdateKey() {
        manager.clearPlugins();
        Assertions.assertThrows(RuntimeException.class, () -> loadAlternate(MockMissingKeyAddon.class));
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void testSuperEnable() {
        manager.clearPlugins();
        Assertions.assertDoesNotThrow(() -> loadAlternate(MockSuperEnableAddon.class));
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void testSuperDisable() {
        manager.clearPlugins();
        Assertions.assertDoesNotThrow(() -> loadAlternate(MockSuperDisableAddon.class));
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void testEnableErrorCaught() {
        manager.clearPlugins();
        Assertions.assertDoesNotThrow(() -> loadAlternate(MockCaughtErrorAddon.class));
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void testBadGithubStrings() {
        manager.clearPlugins();
        Assertions.assertThrows(RuntimeException.class, () -> loadAlternate(MockFailAddon.class));
    }

    private static void loadAlternate(Class<? extends MockAddon> clazz) throws InvalidDescriptionException {
        String pluginYML = "name: MockAddon\n" +
                "version: MockVersion\n" +
                "main: " + clazz.getName() + '\n' +
                "commands:\n" +
                "  mockaddon:\n" +
                "    description: MockCommand\n";
        MockBukkit.loadWith(clazz, new PluginDescriptionFile(new StringReader(pluginYML)));
    }

}
