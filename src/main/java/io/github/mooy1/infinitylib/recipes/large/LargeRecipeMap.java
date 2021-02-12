package io.github.mooy1.infinitylib.recipes.large;

import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * A recipe map which supports recipe of any size and checks input amounts
 * 
 * @author Mooy1
 */
public final class LargeRecipeMap {

    private final Map<LargeRecipe, LargeOutput> map = new HashMap<>();
    private final int size;
    
    public LargeRecipeMap(int recipeSize) {
        Validate.isTrue(recipeSize > 0);
        this.size = recipeSize;
    }
    
    public void put(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        Validate.notNull(input);
        Validate.isTrue(input.length == this.size);
        Validate.notNull(output);
        
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

    public int size() {
        return this.map.size();
    }

    /**
     * A large recipe which stores a string of all ids/materials and amounts
     */
    private static final class LargeRecipe {
        
        private final String string;
        private final int[] amounts;
        
        private LargeRecipe(@Nonnull ItemStack[] recipe) {
            StringBuilder builder = new StringBuilder();
            int[] amounts = new int[recipe.length];
            
            for (int i = 0 ; i < recipe.length ; i++) {
                if (recipe[i] != null) {
                    builder.append(StackUtils.getIDorType(recipe[i])).append(';');
                    amounts[i] = recipe[i].getAmount();
                }
            }
            
            this.string = builder.toString();
            this.amounts = amounts;
        }

        @Override
        public int hashCode() {
            return this.string.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LargeRecipe)) return false;
            LargeRecipe input = (LargeRecipe) obj;
            for (int i = 0 ; i < this.amounts.length ; i++) {
                if (input.amounts[i] < this.amounts[i]) {
                    return false;
                }
            }
            return input.string.equals(this.string);
        }

    }
    
}
