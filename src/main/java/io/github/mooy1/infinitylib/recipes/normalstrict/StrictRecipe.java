package io.github.mooy1.infinitylib.recipes.normalstrict;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A strict recipe which checks id/material and amount
 */
final class StrictRecipe {

    private final String id;
    private final int amount;

    StrictRecipe(@Nonnull ItemStack item) {
        this.id = StackUtils.getIDorType(item);
        this.amount = item.getAmount();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StrictRecipe)) return false;
        StrictRecipe recipe = (StrictRecipe) obj;
        return recipe.amount <= this.amount && recipe.id.equals(this.id);
    }

}
