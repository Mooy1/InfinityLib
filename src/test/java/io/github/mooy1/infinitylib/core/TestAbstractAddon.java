package io.github.mooy1.infinitylib.core;

import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestAbstractAddon {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
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
        Assertions.assertNotNull(MockAddon.createKey("test"));
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
        Assertions.assertDoesNotThrow(() -> MockAddon.log(Level.INFO));
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
    }

    @Test
    void testSharedInfinityLib() {
        Assertions.assertThrows(IllegalStateException.class, () -> MockBukkit.load(MockAddon.class));
        Assertions.assertSame(addon, MockAddon.instance());
    }

    @Test
    void testInstance() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
    }

}
