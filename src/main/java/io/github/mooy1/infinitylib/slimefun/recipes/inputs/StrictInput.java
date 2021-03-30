package io.github.mooy1.infinitylib.slimefun.recipes.inputs;

import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.slimefun.recipes.RecipeInput;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A strict recipe which checks id/material and amount
 */
public class StrictInput extends RecipeInput {

    private final String id;
    @Getter
    private final int amount;

    public StrictInput(@Nonnull ItemStack item) {
        this.id = StackUtils.getIDorType(item);
        this.amount = item.getAmount();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StrictInput)) return false;
        StrictInput recipe = (StrictInput) obj;
        return recipe.amount <= this.amount && recipe.id.equals(this.id);
    }

}
