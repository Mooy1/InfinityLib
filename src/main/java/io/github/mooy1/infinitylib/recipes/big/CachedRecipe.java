package io.github.mooy1.infinitylib.recipes.big;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CachedRecipe extends BigRecipe {

    private final ItemStack output;
    
    CachedRecipe(@Nonnull ItemStack[] recipe, ItemStack output) {
        super(recipe);
        this.output = output;
    }
    
    @Nullable
    public ItemStack get(@Nonnull ItemStack[] input) {
        return equals(new BigRecipe(input)) ? this.output : null;
    }
    
    @Nullable
    public OutputAndAmounts get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Pair<BigRecipe, int[]> pair = fromMenu(menu, slots);
        return equals(pair.getFirstValue()) ? new OutputAndAmounts(this.output, pair.getSecondValue()) : null;
    }
    
}
