package io.github.mooy1.infinitylib.slimefun.presets;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Presets for making recipes
 * 
 * @author Mooy1
 */
@UtilityClass
public final class RecipePreset {

    @Nonnull
    public static ItemStack[] Compress(@Nonnull ItemStack item) {
        return new ItemStack[]{
                item, item, item, item, item, item, item, item, item,
        };
    }

    @Nonnull
    public static ItemStack[] middleItem(@Nonnull ItemStack item) {
        return new ItemStack[] {
                null, null, null, null , item, null, null, null, null
        };
    }
    
    @Nonnull
    public static ItemStack[] firstItem(@Nonnull ItemStack item) {
        return new ItemStack[] {
                item, null, null, null, null, null, null, null, null
        };
    }
    
}
