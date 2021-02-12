package io.github.mooy1.infinitylib.recipes.normal;

import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class RandomRecipeMap {
    
    private final Map<Recipe, RandomizedSet<ItemStack>> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack[] outputs, @Nonnull float[] weights) {
        RandomizedSet<ItemStack> set = new RandomizedSet<>();
        for (int i = 0 ; i < outputs.length ;i++) {
            set.add(outputs[i], weights[i]);
        }
        this.map.put(new Recipe(item), set);
    }

    public void put(@Nonnull ItemStack item, @Nonnull RandomizedSet<ItemStack> set) {
        this.map.put(new Recipe(item), set);
    }

    @Nullable
    public ItemStack get(@Nonnull ItemStack item) {
        RandomizedSet<ItemStack> set = this.map.get(new Recipe(item));
        return set == null ? null : set.getRandom();
    }

    public int size() {
        return this.map.size();
    }
    
}
