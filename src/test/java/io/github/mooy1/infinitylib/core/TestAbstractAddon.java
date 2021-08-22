package io.github.mooy1.infinitylib.core;

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
    void testNotNullEnvironment() {
        Assertions.assertNotNull(MockAddon.environment());
    }

    @Test
    void testNotNullConfig() {
        Assertions.assertNotNull(MockAddon.config());
    }

    @Test
    void testCreateKey() {
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
    void testGlobalTick() {
        server.getScheduler().performOneTick();
        Assertions.assertEquals(1, MockAddon.slimefunTickCount());
    }

    @Test
    void testCommand() {
        Assertions.assertNotNull(MockAddon.instance().getCommand());
        server.executeConsole("mockaddon").assertSucceeded();
    }

    @Test
    void testInstance() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
    }

    @Test
    void testDuplicateInstance() {
        Assertions.assertThrows(RuntimeException.class, () -> MockBukkit.load(MockAddon.class));
    }

    @Test
    void testNotRelocatedLive() {
        Assertions.assertThrows(RuntimeException.class, () -> MockBukkit.load(MockAddon.class, Environment.LIVE, null));
    }

}
