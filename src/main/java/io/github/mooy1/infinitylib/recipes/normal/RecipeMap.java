package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class RecipeMap {

    private final Map<Recipe, ItemStack> map = new HashMap<>();
    
    public void put(@Nonnull ItemStack item, @Nonnull ItemStack output) {
        this.map.put(new Recipe(item), output);
    }
    
    @Nonnull
    public ItemStack get(@Nonnull ItemStack item) {
        return this.map.get(new Recipe(item));
    }

    public int size() {
        return this.map.size();
    }
    
    private static final class Recipe {

        private final String id;

        private Recipe(@Nonnull ItemStack item) {
            this.id = StackUtils.getIDorType(item);
        }

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Recipe && ((Recipe) obj).id.equals(this.id);
        }
    }

}
