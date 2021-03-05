package io.github.mooy1.infinitylib.recipes.large;

import io.github.mooy1.infinitylib.recipes.AbstractRecipeMap;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A recipe map which supports recipe of any size and checks input amounts
 * 
 * @author Mooy1
 */
public final class LargeRecipeMap extends AbstractRecipeMap<LargeRecipe, ItemStack> {

    private final int size;
    
    public LargeRecipeMap(int recipeSize) {
        Validate.isTrue(recipeSize > 0);
        this.size = recipeSize;
    }
    
    public void put(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        Validate.isTrue(input.length == this.size);
        this.recipes.put(new LargeRecipe(input), output);
    }

    @Nullable
    public ItemStack get(@Nonnull ItemStack[] input) {
        Validate.isTrue(input.length == this.size);
        return this.recipes.get(new LargeRecipe(input));
    }

    @Nullable
    public ItemStack get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Validate.isTrue(slots.length == this.size);
        return this.recipes.get(new LargeRecipe(menu, slots));
    }
    
}
