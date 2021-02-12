package io.github.mooy1.infinitylib.recipes;

import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class StrictRecipeMap {
    
    private final Map<Recipe, Pair<ItemStack, Integer>> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        this.map.put(new Recipe(item), new Pair<>(output, item.getAmount()));
    }

    @Nonnull
    public Pair<ItemStack, Integer> get(@Nonnull ItemStack item) {
        return this.map.get(new Recipe(item));
    }

    private static final class Recipe {

        private final String id;
        private final int amount;

        private Recipe(@Nonnull ItemStack item) {
            this.id = StackUtils.getIDorType(item);
            this.amount = hashCode();
        }

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Recipe)) return false;
            Recipe recipe = (Recipe) obj;
            return recipe.amount >= this.amount && recipe.id.equals(this.id);
        }

    }

}
