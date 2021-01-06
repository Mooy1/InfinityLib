package io.github.mooy1.infinitylib.math;

public final class MathUtils {

    public static int mod(int a, int b) {
        return a & (b - 1);
    }
    
    public static int div(int a, int b) {
        return a >> log2(b - 1);
    }
    
    public static int exp(int a, int b) {
        int i = 1;
        while (b > 0) {
            i*= a;
            b--;
        }
        return i;
    }
    
    public static int log2(int a) {
        return 31 - Integer.numberOfLeadingZeros(a);
    }
    
}
