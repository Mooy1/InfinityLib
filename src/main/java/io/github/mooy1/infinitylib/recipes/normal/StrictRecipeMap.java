package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class StrictRecipeMap {
    
    private final Map<StrictRecipe, OutputAndAmount> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        this.map.put(new StrictRecipe(item), new OutputAndAmount(output, item.getAmount()));
    }

    @Nonnull
    public OutputAndAmount get(@Nonnull ItemStack item) {
        return this.map.get(new StrictRecipe(item));
    }

    public int size() {
        return this.map.size();
    }

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
            StrictRecipe recipe = (StrictRecipe) obj;
            return recipe.amount >= this.amount && recipe.id.equals(this.id);
        }
    
    }

}
