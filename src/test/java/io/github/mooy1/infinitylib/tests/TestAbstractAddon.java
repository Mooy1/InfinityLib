package io.github.mooy1.infinitylib.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.configuration.AddonConfig;

class TestAbstractAddon {

    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testBugTrackerURL() {
        Assertions.assertEquals("https://github.com/Mooy1/InfinityLib/issues", addon.getBugTrackerURL());
    }

    @Test
    void testDefaultAutoUpdate() {
        Assertions.assertTrue(addon.getConfig().getBoolean(addon.getAutoUpdatePath()));
        Assertions.assertEquals(AddonConfig.AUTO_UPDATE_COMMENT, addon.getConfig().getComment(addon.getAutoUpdatePath()));
    }

}
