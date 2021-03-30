package io.github.mooy1.infinitylib.slimefun.recipes;

import org.bukkit.inventory.ItemStack;

public final class SimpleRecipeMap<I extends RecipeInput> extends RecipeMap<I, ItemStack> {

    @Override
    public void put(I input, ItemStack output) {
        this.recipes.put(input, output);
    }

}
