package io.github.mooy1.infinitylib.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertNotNull(MockAddon.environment());
    }

    @Test
    void testNotNullConfig() {
        assertNotNull(MockAddon.config());
    }

    @Test
    void testCreateKey() {
        assertNotNull(MockAddon.createKey("test"));
    }

    @Test
    void testAutoUpdatedDisabled() {
        assertFalse(addon.autoUpdatesEnabled());
    }

    @Test
    void testBugTrackerURL() {
        assertEquals("https://github.com/Mooy1/InfinityLib/issues", addon.getBugTrackerURL());
    }

    @Test
    void testGlobalTick() {
        server.getScheduler().performOneTick();
        assertEquals(1, MockAddon.slimefunTickCount());
    }

    @Test
    void testCommand() {
        assertNotNull(MockAddon.instance().getAddonCommand());
        server.executeConsole("mockaddon").assertSucceeded();
    }

    @Test
    void testInstance() {
        assertDoesNotThrow((ThrowingSupplier<Object>) MockAddon::instance);
    }

    @Test
    void testDuplicateInstance() {
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockAddon.class));
    }

    @Test
    void testNotRelocatedLive() {
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockAddon.class, Environment.LIVE, null));
    }

}
