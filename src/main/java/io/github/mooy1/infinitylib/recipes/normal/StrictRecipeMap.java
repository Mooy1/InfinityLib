package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A recipe map which checks that the input has at least the recipes item amount
 * 
 * @author Mooy1
 */
public final class StrictRecipeMap {
    
    private final Map<StrictRecipe, StrictOutput> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        Validate.notNull(item);
        Validate.notNull(output);
        this.map.put(new StrictRecipe(item), new StrictOutput(output, item.getAmount()));
    }

    @Nonnull
    public StrictOutput get(@Nonnull ItemStack item) {
        return this.map.get(new StrictRecipe(item));
    }

    public int size() {
        return this.map.size();
    }

    /**
     * A strict recipe which checks id/material and amount
     */
    private static final class StrictRecipe {
    
        private final String id;
        private final int amount;

        private StrictRecipe(@Nonnull ItemStack item) {
            this.id = StackUtils.getIDorType(item);
            this.amount = hashCode();
        }
    
        @Override
        public int hashCode() {
            return this.id.hashCode();
        }
    
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StrictRecipe)) return false;
            StrictRecipe input = (StrictRecipe) obj;
            return input.amount >= this.amount && input.id.equals(this.id);
        }
    
    }

}
