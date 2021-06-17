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
import io.github.mooy1.infinitylib.mocks.MockUtils;

class TestAbstractAddon {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockUtils.mock(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testOfficialBuild() {
        Assertions.assertFalse(addon.isOfficialBuild());
    }

    @Test
    void testNotTesting() {
        Assertions.assertFalse(addon.isNotTesting());
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

    @Test
    void testEnableErrorCaught() {
        Assertions.assertDoesNotThrow(() -> MockUtils.mock(MockErrorAddon.class));
    }

    @Test
    void testMetricsNotCreated() {
        Assertions.assertFalse(MockUtils.mock(MockMetricsAddon.class).isMetricsCreated());
    }

    @Test
    void testSubCommands() {
        MockBukkit.load(MockCommandAddon.class);
        server.executeConsole("mockaddon", "test").assertSucceeded();
    }

    @Test
    void testBadGithubPath() {
        Assertions.assertThrows(RuntimeException.class, () -> MockUtils.mock(MockFailAddon.class));
    }

    @Test
    void testBugTrackerURL() {
        Assertions.assertEquals("https://github.com/Mooy1/InfinityLib/issues", addon.getBugTrackerURL());
    }

}
