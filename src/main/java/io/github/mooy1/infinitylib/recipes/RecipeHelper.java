package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

/**
 * Collection of common for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
// TODO rename/move?
@UtilityClass
@ParametersAreNonnullByDefault
public final class RecipeHelper {

    // TODO move into machine classes
    @Nonnull
    public static ItemStack[] arrayFrom(DirtyChestMenu menu, int[] slots) {
        ItemStack[] arr = new ItemStack[slots.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = menu.getItemInSlot(slots[i]);
        }
        return arr;
    }

}
