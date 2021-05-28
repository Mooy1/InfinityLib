package io.github.mooy1.infinitylib.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.configuration.AddonConfig;

class TestConfiguration {

    private static TestAddon addon;
    private static AddonConfig config;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(TestAddon.class);
        config = new AddonConfig(addon, "test.yml");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testNoDefaults() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new AddonConfig(addon, "fail.yml"));
    }

    @Test
    void testComments() {
        Assertions.assertEquals("\n# test\n", config.getComment("test"));
        Assertions.assertEquals("\n  # test\n", config.getComment("section.test"));
    }

    @Test
    void testSave() {
        String correct = "\n# test\ntest: test\nsection:\n\n  # test\n  test: test\n";
        Assertions.assertEquals(config.saveToString(), correct);
    }

}
