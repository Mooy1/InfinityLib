package io.github.mooy1.infinitylib.recipes.normalstrict;

import io.github.mooy1.infinitylib.recipes.AbstractRecipeMap;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A recipe map which checks that the input has at least the recipes item amount
 * 
 * @author Mooy1
 */
public final class StrictRecipeMap extends AbstractRecipeMap<StrictRecipe, StrictOutput> {
    
    protected void add(@Nonnull ItemStack recipe, @Nonnull ItemStack output) {
        this.recipes.put(new StrictRecipe(recipe), new StrictOutput(output, recipe.getAmount()));
    }
    
    @Nonnull
    public StrictOutput get(@Nonnull ItemStack item) {
        return this.recipes.get(new StrictRecipe(item));
    }
    
}
