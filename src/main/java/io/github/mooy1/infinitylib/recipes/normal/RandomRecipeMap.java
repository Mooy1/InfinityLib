package io.github.mooy1.infinitylib.recipes.normal;

import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class RandomRecipeMap {
    
    private final Map<Recipe, RandomizedSet<ItemStack>> map = new HashMap<>();

    public void put(@Nonnull ItemStack item,
                    ItemStack a, float b) {
        put(item, new ItemStack[]{a}, new float[] {b});
    }
    
    public void put(@Nonnull ItemStack item,
                    ItemStack a, float b,
                    ItemStack c, float d) {
        put(item, new ItemStack[]{a, c}, new float[] {b, d});
    }

    public void put(@Nonnull ItemStack item,
                    ItemStack a, float b,
                    ItemStack c, float d,
                    ItemStack e, float f) {
        put(item, new ItemStack[]{a, c, e}, new float[] {b, d, f});
    }
    
    public void put(@Nonnull ItemStack item,
                    ItemStack a, float b,
                    ItemStack c, float d,
                    ItemStack e, float f,
                    ItemStack g, float h) {
        put(item, new ItemStack[]{a, c, e, g}, new float[] {b, d, f, h});
    }
    
    public void put(@Nonnull ItemStack item,
                    ItemStack a, float b,
                    ItemStack c, float d,
                    ItemStack e, float f,
                    ItemStack g, float h,
                    ItemStack i, float j) {
        put(item, new ItemStack[]{a, c, e, g, i}, new float[] {b, d, f, h, j});
    }

    public void put(@Nonnull ItemStack item,
                    ItemStack a, float b,
                    ItemStack c, float d,
                    ItemStack e, float f,
                    ItemStack g, float h,
                    ItemStack i, float j,
                    ItemStack k, float l) {
        put(item, new ItemStack[]{a, c, e, g, i, k}, new float[] {b, d, f, h, j, l});
    }
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack[] outputs, @Nonnull float[] weights) {
        RandomizedSet<ItemStack> set = new RandomizedSet<>();
        for (int i = 0 ; i < outputs.length ;i++) {
            set.add(outputs[i], weights[i]);
        }
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
