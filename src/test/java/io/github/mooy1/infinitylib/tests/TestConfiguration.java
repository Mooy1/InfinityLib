package io.github.mooy1.infinitylib.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.configuration.AddonConfig;
import io.github.mooy1.infinitylib.mocks.MockAddon;

class TestConfiguration {

    private static MockAddon addon;
    private static AddonConfig config;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        config = new AddonConfig(addon, "test.yml");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testComments() {
        Assertions.assertEquals("\n# test\n", config.getComment("test"));
        Assertions.assertEquals("\n  # test\n", config.getComment("section.test"));
    }

    @Test
    void testSave() {
        String correct = "\n# test\ntest: test\n\nsection:\n\n  # test\n  test: test\n";
        Assertions.assertEquals(correct, config.saveToString());
    }

    @Test
    void testNoDefaults() {
        AddonConfig fail = new AddonConfig(addon, "fail.yml");
        Assertions.assertNotNull(fail.getDefaults());
        Assertions.assertEquals(fail.getDefaults().getKeys(true).size(), 0);
    }

}
