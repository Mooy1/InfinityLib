package io.github.mooy1.infinitylib.tests;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class TestUtils {

    public static double time(Runnable runnable) {
        long before = System.nanoTime();
        for (int i = 0 ; i < 1000 ; i++) {
            runnable.run();
        }
        long after = System.nanoTime();
        return (after - before) / 1000D;
    }

}
