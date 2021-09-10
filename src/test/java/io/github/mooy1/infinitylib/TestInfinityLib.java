package io.github.mooy1.infinitylib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestInfinityLib {

    @Test
    void testVersion() {
        assertNotEquals("${project.version}", InfinityLib.VERSION);
    }

    @Test
    void testPackage() {
        assertEquals("io.github.mooy1.infinitylib", InfinityLib.PACKAGE);
    }

    @Test
    void testAddonPackage() {
        assertEquals("io.github.mooy1", InfinityLib.ADDON_PACKAGE);
    }

}
