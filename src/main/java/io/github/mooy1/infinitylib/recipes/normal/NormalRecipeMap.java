package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.misc.MapHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A simple input-output recipe map
 * 
 * @author Mooy1
 */
public final class NormalRecipeMap extends MapHolder<NormalRecipe, ItemStack> {
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        this.map.put(new NormalRecipe(item), output);
    }
    
    @Nonnull
    public ItemStack get(@Nonnull ItemStack item) {
        return this.map.get(new NormalRecipe(item));
    }

}
