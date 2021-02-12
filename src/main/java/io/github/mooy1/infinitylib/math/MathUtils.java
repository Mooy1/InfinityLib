package io.github.mooy1.infinitylib.math;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;

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
     * Integer to array of all its digits
     */
    @Nonnull
    public static int[] toArray(int i) {
        String string = String.valueOf(i);
        int[] arr = new int[string.length()];
        for (int j = 0 ; j < string.length() ; j++) {
            arr[j] = string.charAt(j) - '0';
        }
        return arr;
    }

    /**
     * All combinations of an array, returned array has a size of the input array factorial
     * Done by repeatedly swapping certain indexes until all combinations have been reached
     */
    @Nonnull
    public static <T> T[][] combinations(@Nonnull T[] array) {
        Class<?> arrayClass = array.getClass();
        Class<?> componentClass = arrayClass.getComponentType();
        int length = array.length;
        @SuppressWarnings("unchecked")
        T[][] combos = (T[][]) Array.newInstance(arrayClass, fact(length));
        if (length == 1) {
            combos[0] = array;
        } else if (length != 0) {
            T[] old = array;
            int index = 0;
            while (index < combos.length) {
                for (int swap = 0 ; swap < length - 1 ; swap++) {
                    @SuppressWarnings("unchecked")
                    T[] current = (T[]) Array.newInstance(componentClass, length);
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
        return combos;
    }

}