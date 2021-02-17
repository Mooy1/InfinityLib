package io.github.mooy1.infinitylib.math;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Collection of utils for randomization
 *
 * @author Mooy1
 */
@UtilityClass
public final class RandomUtils {
    
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    /**
     * This method returns a random int from the given range, inclusive
     *
     * @param min minimum int, inclusive
     * @param max maximum int, inclusive
     * @return random int in range
     */
    public static int randomFromRange(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    /**
     * This method gets a random ItemStack from the array
     */
    @Nonnull
    public static ItemStack randomOutput(@Nonnull ItemStack[] array) {
        return array[random.nextInt(array.length)].clone();
    }

    /**
     * This method will return true 1 / chance times.
     */
    public static boolean chanceIn(int chance) {
        return random.nextInt(chance) == 0;
    }
    
}