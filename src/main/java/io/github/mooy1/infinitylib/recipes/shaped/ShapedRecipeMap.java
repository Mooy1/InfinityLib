package io.github.mooy1.infinitylib.recipes.shaped;

import io.github.mooy1.infinitylib.misc.MapHolder;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A recipe map with 3x3 shaped (or shapeless) recipes. It will detect the shape of recipes on its own.
 * 
 * @author Mooy1
 */
public final class ShapedRecipeMap extends MapHolder<ShapedRecipe, ItemStack> {
    
    public void put(@Nonnull ItemStack[] recipe, @Nonnull ItemStack output) {
        Validate.isTrue(recipe.length == 9);
        
        this.map.put(new ShapedRecipe(recipe), output);
    }
    
    @Nullable
    public ShapedOutput get(@Nonnull ItemStack[] input) {
        Validate.isTrue(input.length == 9);
        
        int[] amounts = new int[9];
        for (int i = 0 ; i < 9 ; i++) {
            if (input[i] != null) {
                amounts[i] = input[i].getAmount();
            }
        }
        
        ItemStack output = this.map.get(new ShapedRecipe(input));
        return output == null ? null : new ShapedOutput(output, amounts);
    }
    
    @Nullable
    public ShapedOutput get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Validate.isTrue(slots.length == 9);
        
        ItemStack[] stacks = new ItemStack[9];
        int[] amounts = new int[9];
        for (int i = 0 ; i < 9 ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                amounts[i] = (stacks[i] = item).getAmount();
            }
        }
        
        ItemStack output = this.map.get(new ShapedRecipe(stacks));
        return output == null ? null : new ShapedOutput(output, amounts);
    }

}
