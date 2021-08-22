package io.github.mooy1.infinitylib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestInfinityLib {

    @Test
    void testVersion() {
        Assertions.assertNotEquals("${project.version}", InfinityLib.VERSION);
    }

    @Test
    void testPackage() {
        Assertions.assertEquals("io.github.mooy1.infinitylib", InfinityLib.PACKAGE);
    }

    @Test
    void testAddonPackage() {
        Assertions.assertEquals("io.github.mooy1", InfinityLib.ADDON_PACKAGE);
    }

}
