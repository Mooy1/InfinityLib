package io.github.mooy1.infinitylib.math;

import lombok.experimental.UtilityClass;

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
     * Modulo when b is a power of 2
     */
    public static int modPow2(int a, int b) {
        return a & (b - 1);
    }

    /**
     * Powers of 2 for positive exponents
     */
    public static int twoPowOf(int b) {
        return 1 << b;
    }
    
    /**
     * Finds log base 2 of a power of 2 exactly or the ceiling for any other number.
     */
    public static int ceilLog2(int a) {
        return 32 - Integer.numberOfLeadingZeros(a - 1);
    }

    /**
     * Rounds up to the nearest power of 2 that is greater than or equal to itself
     * returns 1 for non positive values
     * returns MIN_VALUE for values > 2^30
     *
     * MAX_VALUE -> MIN_VALUE
     * 3 -> 4
     * 2 -> 2
     * 1 -> 1
     * 0 -> 1
     * -1 -> 1
     * -2 -> 1
     * -3 -> 1
     * MIN_VALUE -> 1
     *
     */
    public static int roundUpToPow2(int a) {
        return 1 << ceilLog2(a);
    }

    /**
     * Finds log base 2 of a power of 2 exactly or the floor for any other number.
     */
    public static int floorLog2(int a) {
        return 31 - Integer.numberOfLeadingZeros(a);
    }
    
    /**
     * Rounds down to the nearest power of 2 that is less than or equal to itself
     * returns MIN_VALUE for non positive values
     * returns correctly for all positive values
     *
     * MAX_VALUE -> 2^30
     * 3 -> 2
     * 2 -> 2
     * 1 -> 1
     * 0 -> MIN_VALUE
     * -1 -> MIN_VALUE
     * -2 -> MIN_VALUE
     * -3 -> MIN_VALUE
     * MIN_VALUE -> MIN_VALUE
     * 
     */
    public static int roundDownToPow2(int a) {
        return 1 << floorLog2(a);
    }
    
}
