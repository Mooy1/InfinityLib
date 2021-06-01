package io.github.mooy1.infinitylib.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.mooy1.infinitylib.mocks.MockAddon;
import io.github.mooy1.infinitylib.mocks.MockMetrics;

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
    void testCatchEnableThrowable() {
        Assertions.assertDoesNotThrow(() -> MockBukkit.load(MockAddon.class));
    }

    @Test
    void testBugTrackerURL() {
        Assertions.assertEquals("https://github.com/Mooy1/InfinityLib/issues", addon.getBugTrackerURL());
    }

    @Test
    void testSubCommands() {
        server.executeConsole("mockaddon", "test").assertSucceeded();
    }

    @Test
    void testConfig() {
        Assertions.assertNotNull(addon.getConfig());
    }

    @Test
    void testGlobalTick() {
        server.getScheduler().performOneTick();
        Assertions.assertEquals(1, addon.getGlobalTick());
    }

    @Test
    void testNoMetrics() {
        Assertions.assertFalse(MockMetrics.created);
    }

}
