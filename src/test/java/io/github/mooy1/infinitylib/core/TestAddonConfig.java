package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void testAddonConfigWithNoDefaults() {
        assertDoesNotThrow(() -> new AddonConfig("defaults.yml"));
    }

    @Test
    void testDefaultConfigs() {
        assertNotNull(config.getDefaults());
        assertFalse(config.getDefaults().getKeys(true).isEmpty());
        assertNotNull(new AddonConfig(new File("test.yml")).getDefaults());
    }

    @Test
    void testComments() {
        assertEquals("\n# test\n", config.getComment("test"));
        assertEquals("\n# line a\n# line b\n", config.getComment("section"));
        assertEquals("\n  # test\n", config.getComment("section.test"));
    }

    @Test
    void testRemoveUnusedKeys() {
        config.set("unused", "unused");
        int keys = config.getKeys(true).size();
        config.removeUnusedKeys();
        Assertions.assertEquals(keys - 1, config.getKeys(true).size());
    }

    @Test
    void testGetIntFromRange() {
        config.set("int", 2);
        assertEquals(1, config.getInt("int", 1, 1));
        config.set("int", 2);
        assertEquals(2, config.getInt("int", 1, 3));
        config.set("int", 2);
        assertEquals(3, config.getInt("int", 3, 3));
        config.set("int", null);
    }

    @Test
    void testGetDoubleFromRange() {
        config.set("double", 2.0);
        assertEquals(1.0, config.getDouble("double", 1.0, 1.0));
        config.set("double", 2.0);
        assertEquals(2.0, config.getDouble("double", 1.0, 3.0));
        config.set("double", 2.0);
        assertEquals(3.0, config.getDouble("double", 3.0, 3.0));
        config.set("double", null);
    }

    @Test
    void testSaveToString() {
        String correct = "\n" +
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
        assertEquals(correct, config.saveToString());
    }

}
