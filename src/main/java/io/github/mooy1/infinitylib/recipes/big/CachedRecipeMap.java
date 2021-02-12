package io.github.mooy1.infinitylib.recipes.big;

import io.github.mooy1.infinitylib.items.StackUtils;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class CachedRecipeMap {

    private final Map<Recipe, CachedRecipe> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack[] recipe, @Nonnull ItemStack output) {
        this.map.put(new Recipe(output), new CachedRecipe(recipe, output));
    }
    
    @Nullable
    public CachedRecipe get(@Nonnull ItemStack item) {
        return this.map.get(new Recipe(item));
    }
    
    @EqualsAndHashCode
    private static final class Recipe {
        
        private final String id;
        private final int amount;
        
        private Recipe(@Nonnull ItemStack item) {
            this.id = StackUtils.getIDorType(item);
            this.amount = item.getAmount();
        }
    }

    public int size() {
        return this.map.size();
    }
    
}
