package io.github.mooy1.infinitylib.recipes.normalstrict;

import io.github.mooy1.infinitylib.recipes.AbstractRecipeMap;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * A randomized recipe map that checks input amount
 * 
 * @author Mooy1
 */
public final class StrictRandomRecipeMap extends AbstractRecipeMap<StrictRecipe, RandomizedSet<StrictOutput>> {
    
    public void put(@Nonnull ItemStack recipe, @Nonnull RandomizedSet<ItemStack> set) {
        RandomizedSet<StrictOutput> recipes = new RandomizedSet<>();
        for (Map.Entry<ItemStack, Float> entry : set.toMap().entrySet()) {
            recipes.add(new StrictOutput(entry.getKey(), recipe.getAmount()), entry.getValue());
        }
        this.recipes.put(new StrictRecipe(recipe), recipes);
    }

    public void put(@Nonnull ItemStack recipe, @Nonnull ItemStack output, float weight) {
        this.recipes.computeIfAbsent(new StrictRecipe(recipe), k -> new RandomizedSet<>()).add(new StrictOutput(output, recipe.getAmount()), weight);
    }
    
    @Nullable
    public StrictOutput get(@Nonnull ItemStack item) {
        RandomizedSet<StrictOutput> set = this.recipes.get(new StrictRecipe(item));
        return set == null ? null : set.getRandom();
    }

}
