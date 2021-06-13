package io.github.mooy1.infinitylib;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    void testEnableErrorCaught() {
        Assertions.assertDoesNotThrow(() -> addon.onEnable());
    }

    @Test
    void testMetricsNotCreated() {
        Assertions.assertFalse(addon.isMetricsCreated());
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
    void testMainConfig() {
        Assertions.assertNotNull(addon.getConfig());
    }

    @Test
    void testGlobalTick() {
        server.getScheduler().performOneTick();
        Assertions.assertEquals(1, addon.getGlobalTick());
    }

}
