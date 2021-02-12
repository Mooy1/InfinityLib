package io.github.mooy1.infinitylib.recipes.normal;

import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * A input-output recipe map with random outputs
 * 
 * @author Mooy1
 */
public final class RandomRecipeMap {
    
    private final Map<NormalRecipe, RandomizedSet<ItemStack>> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack[] outputs, @Nonnull float[] weights) {
        RandomizedSet<ItemStack> set = new RandomizedSet<>();
        for (int i = 0 ; i < outputs.length ;i++) {
            set.add(outputs[i], weights[i]);
        }
        put(item, set);
    }

    public void put(@Nonnull ItemStack item, @Nonnull RandomizedSet<ItemStack> set) {
        Validate.notNull(item);
        Validate.notNull(set);
        this.map.put(new NormalRecipe(item), set);
    }

    @Nullable
    public ItemStack get(@Nonnull ItemStack item) {
        RandomizedSet<ItemStack> set = this.map.get(new NormalRecipe(item));
        return set == null ? null : set.getRandom();
    }

    public int size() {
        return this.map.size();
    }
    
}
