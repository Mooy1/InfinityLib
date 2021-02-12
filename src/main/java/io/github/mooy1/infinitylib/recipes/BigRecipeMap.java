package io.github.mooy1.infinitylib.recipes;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class BigRecipeMap {
    
    private final Map<BigRecipe, ItemStack> map = new HashMap<>();

    public void put(@Nonnull ItemStack[] recipe, @Nonnull ItemStack output) {
        this.map.put(new BigRecipe(recipe), output);
    }
    
    @Nullable
    public ItemStack get(@Nonnull ItemStack[] input) {
        return this.map.get(new BigRecipe(input));
    }
    
    @Nullable
    public Pair<ItemStack, int[]> get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Pair<BigRecipe, int[]> pair = BigRecipe.fromMenu(menu, slots);
        ItemStack output = this.map.get(pair.getFirstValue());
        return output == null ? null : new Pair<>(output, pair.getSecondValue());
    }

}
