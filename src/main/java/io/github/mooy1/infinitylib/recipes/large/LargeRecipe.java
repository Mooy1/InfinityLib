package io.github.mooy1.infinitylib.recipes.large;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A large recipe which stores a string of all ids/materials and amounts
 */
final class LargeRecipe {

    private final String string;
    private final int[] amounts;

    LargeRecipe(@Nonnull ItemStack[] recipe) {
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
