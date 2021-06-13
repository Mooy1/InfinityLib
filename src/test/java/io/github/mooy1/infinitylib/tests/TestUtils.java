package io.github.mooy1.infinitylib.tests;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class TestUtils {

    public static void time(String test, Runnable runnable) {
        long before = System.nanoTime();
        for (int i = 0 ; i < 1000 ; i++) {
            runnable.run();
        }
        long after = System.nanoTime();
        System.out.println(test + ": " + ((after - before) / 1000D) + "ns");
    }

}
