package io.github.mooy1.infinitylib.recipes.largestrict;

import io.github.mooy1.infinitylib.misc.MapHolder;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class StrictLargeRecipeMap extends MapHolder<StrictLargeRecipe, StrictLargeOutput> {

    private final int size;

    public StrictLargeRecipeMap(int recipeSize) {
        Validate.isTrue(recipeSize > 0);
        this.size = recipeSize;
    }

    public void put(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        Validate.isTrue(input.length == this.size);
        StrictLargeRecipe recipe = new StrictLargeRecipe(input);
        this.map.put(recipe, new StrictLargeOutput(output, recipe.amounts));
    }

    @Nullable
    public StrictLargeOutput get(@Nonnull ItemStack[] input) {
        Validate.isTrue(input.length == this.size);
        return this.map.get(new StrictLargeRecipe(input));
    }

    @Nullable
    public StrictLargeOutput get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Validate.isTrue(slots.length == this.size);
        return this.map.get(new StrictLargeRecipe(menu, slots));
    }
    
}
