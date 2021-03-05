package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.recipes.AbstractRecipeMap;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A input-output recipe map with random outputs
 * 
 * @author Mooy1
 */
public final class RandomRecipeMap extends AbstractRecipeMap<NormalRecipe, RandomizedSet<ItemStack>> {
    
    public void put(@Nonnull ItemStack item, @Nonnull RandomizedSet<ItemStack> set) {
        this.recipes.put(new NormalRecipe(item), set);
    }

    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output, float weight) {
        this.recipes.computeIfAbsent(new NormalRecipe(item), k -> new RandomizedSet<>()).add(output, weight);
    }

    @Nullable
    public ItemStack get(@Nonnull ItemStack item) {
        RandomizedSet<ItemStack> set = this.recipes.get(new NormalRecipe(item));
        return set == null ? null : set.getRandom();
    }
    
}
