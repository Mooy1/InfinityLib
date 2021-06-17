package io.github.mooy1.infinitylib.configuration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.mocks.MockAddon;
import io.github.mooy1.infinitylib.mocks.MockUtils;

class TestAddonConfig {

    private static MockAddon addon;
    private static AddonConfig config;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockUtils.mock(MockAddon.class);
        config = new AddonConfig(addon, "test.yml");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testComments() {
        Assertions.assertEquals("\n# test\n", config.getComment("test"));
        Assertions.assertEquals("\n# line a\n# line b\n", config.getComment("section"));
        Assertions.assertEquals("\n  # test\n", config.getComment("section.test"));
    }

    @Test
    void testSaveToString() {
        String correct =
                "\n" +
                "# test\n" + 
                "test: test\n" + 
                "\n" + 
                "# line a\n" + 
                "# line b\n" + 
                "section:\n" + 
                "\n" + 
                "  # test\n" + 
                "  list:\n" + 
                "  - a\n" + 
                "  - b\n" + 
                "\n" + 
                "  # test\n" + 
                "  test: test\n";
        Assertions.assertEquals(correct, config.saveToString());
    }

    @Test
    void testNoDefaults() {
        Assertions.assertThrows(IllegalStateException.class, () -> new AddonConfig(addon, "fail.yml"));
    }

}
