package io.github.mooy1.infinitylib;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.mooy1.infinitylib.mocks.MockAddon;
import io.github.mooy1.infinitylib.mocks.MockCommandAddon;
import io.github.mooy1.infinitylib.mocks.MockErrorAddon;
import io.github.mooy1.infinitylib.mocks.MockFailAddon;
import io.github.mooy1.infinitylib.mocks.MockMetricsAddon;

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
        Assertions.assertDoesNotThrow(() -> MockBukkit.load(MockErrorAddon.class));
    }

    @Test
    void testMetricsNotCreated() {
        Assertions.assertFalse(MockBukkit.load(MockMetricsAddon.class).isMetricsCreated());
    }

    @Test
    void testBugTrackerURL() {
        Assertions.assertEquals("https://github.com/Mooy1/InfinityLib/issues", addon.getBugTrackerURL());
    }

    @Test
    void testSubCommands() {
        MockBukkit.load(MockCommandAddon.class);
        server.executeConsole("mockaddon", "test").assertSucceeded();
    }

    @Test
    void testBadGithubPath() {
        Assertions.assertThrows(RuntimeException.class, () -> MockBukkit.load(MockFailAddon.class));
    }

    @Test
    void testOfficialBuild() {
        Assertions.assertFalse(addon.isOfficialBuild());
    }

    @Test
    void testNotTesting() {
        Assertions.assertTrue(addon.isNotTesting());
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
