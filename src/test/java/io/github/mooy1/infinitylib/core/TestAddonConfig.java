package io.github.mooy1.infinitylib.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestAddonConfig {

    private static AddonConfig config;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(MockAddon.class);
        config = new AddonConfig("test.yml");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testNoDefaults() {
        Assertions.assertThrows(IllegalStateException.class, () -> new AddonConfig("fail.yml"));
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

}
