package io.github.mooy1.infinitylib.misc;

import com.google.common.math.IntMath;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;

/**
 * Random utility methods
 * 
 * @author Mooy1
 */
@UtilityClass
public final class CollectionUtils {
    
    /**
     * All combinations of an array, returned array has a size of the input array factorial
     * Done by repeatedly swapping certain indexes until all combinations have been reached
     */
    @Nonnull
    public static <T> T[][] combinationsOf(@Nonnull T[] array) {
        Class<?> arrayClass = array.getClass();
        Class<?> componentClass = arrayClass.getComponentType();
        int length = array.length;
        @SuppressWarnings("unchecked")
        T[][] combos = (T[][]) Array.newInstance(arrayClass, IntMath.factorial(length));
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
