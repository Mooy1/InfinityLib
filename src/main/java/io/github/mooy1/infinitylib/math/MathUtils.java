package io.github.mooy1.infinitylib.math;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;

@UtilityClass
public final class MathUtils {
    
    /**
     * Integer exponents
     */
    public static int pow(int a, int b) {
        int i = 1;
        while (b > 0) {
            i*= a;
            b--;
        }
        return i;
    }

    /**
     * Factorial
     */
    public static int fact(int a) {
        return a < 1 ? 0 : a == 1 ? 1 : a * fact(a - 1);
    }

    /**
     * Modulo when b is a power of 2
     */
    public static int modPow2(int a, int b) {
        return a & (b - 1);
    }
    
    /**
     * Finds log base 2 of a power of 2 exactly or the ceiling for any other number.
     */
    public static int ceilLog2(int a) {
        return 32 - Integer.numberOfLeadingZeros(a - 1);
    }

    /**
     * Finds log base 2 of a power of 2 exactly or the floor for any other number.
     */
    public static int floorLog2(int a) {
        return 31 - Integer.numberOfLeadingZeros(a);
    }

    /**
     * All combinations of an array, returned array has a size of the input array factorial
     * Done by repeatedly swapping certain indexes until all combinations have been reached
     */
    public static <T> T[][] combinations(@Nonnull T[] array) {
        int length = array.length;
        Object[][] combos = new Object[fact(length)][length];
        if (length == 1) {
            combos[0] = array;
        } else if (length != 0) {
            Object[] old = array;
            int index = 0;
            while (index < combos.length) {
                for (int swap = 0 ; swap < length - 1 ; swap++) {
                    Object[] current = new Object[length];
                    for (int fill = 0 ; fill < length ; fill++) {
                        if (fill == swap) {
                            current[fill] = old[fill + 1];
                            current[fill + 1] = old[fill];
                            fill++;
                        } else {
                            current[fill] = old[fill];
                        }
                    }
                    combos[index++] = old = current;
                }
            }
        }
        @SuppressWarnings("unchecked")
        T[][] casted = (T[][]) combos;
        return casted;
    }

}