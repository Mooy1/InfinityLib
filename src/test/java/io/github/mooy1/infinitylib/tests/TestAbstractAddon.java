package io.github.mooy1.infinitylib.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.configuration.AddonConfig;

class TestAbstractAddon {

    private static TestAddon addon;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(TestAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testBugTrackerURL() {
        Assertions.assertEquals("https://github.com/Mooy1/TestAddon/issues", addon.getBugTrackerURL());
    }

    @Test
    void testDefaultAutoUpdate() {
        Assertions.assertTrue(addon.getConfig().getBoolean(addon.getAutoUpdatePath()));
        Assertions.assertEquals(addon.getConfig().getComment(addon.getAutoUpdatePath()), AddonConfig.AUTO_UPDATE_COMMENT);
    }

}
