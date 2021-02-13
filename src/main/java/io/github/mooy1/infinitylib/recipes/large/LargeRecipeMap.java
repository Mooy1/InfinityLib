package io.github.mooy1.infinitylib.recipes.large;

import io.github.mooy1.infinitylib.misc.MapHolder;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A recipe map which supports recipe of any size and checks input amounts
 * 
 * @author Mooy1
 */
public final class LargeRecipeMap extends MapHolder<LargeRecipe, LargeOutput> {

    private final int size;
    
    public LargeRecipeMap(int recipeSize) {
        Validate.isTrue(recipeSize > 0);
        this.size = recipeSize;
    }
    
    public void put(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        Validate.isTrue(input.length == this.size);
        
        int[] amounts = new int[9];
        for (int i = 0 ; i < 9 ; i++) {
            if (input[i] != null) {
                amounts[i] = input[i].getAmount();
            }
        }
        
        this.map.put(new LargeRecipe(input), new LargeOutput(output, amounts));
    }

    @Nullable
    public LargeOutput get(@Nonnull ItemStack[] input) {
        Validate.isTrue(input.length == this.size);
        
        return this.map.get(new LargeRecipe(input));
    }

    @Nullable
    public LargeOutput get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Validate.isTrue(slots.length == this.size);
        
        ItemStack[] stacks = new ItemStack[this.size];
        for (int i = 0 ; i < this.size ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                stacks[i] = item;
            }
        }
        
        return this.map.get(new LargeRecipe(stacks));
    }
    
}
