package io.github.mooy1.infinitylib.recipes.normal;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple input-output recipe map
 * 
 * @author Mooy1
 */
public final class NormalRecipeMap {

    private final Map<NormalRecipe, ItemStack> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        this.map.put(new NormalRecipe(item), output);
    }
    
    @Nonnull
    public ItemStack get(@Nonnull ItemStack item) {
        return this.map.get(new NormalRecipe(item));
    }

    public int size() {
        return this.map.size();
    }

}
