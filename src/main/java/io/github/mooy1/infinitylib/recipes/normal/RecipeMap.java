package io.github.mooy1.infinitylib.recipes.normal;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class RecipeMap {

    private final Map<Recipe, ItemStack> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        this.map.put(new Recipe(item), output);
    }
    
    @Nonnull
    public ItemStack get(@Nonnull ItemStack item) {
        return this.map.get(new Recipe(item));
    }

    public int size() {
        return this.map.size();
    }

}
