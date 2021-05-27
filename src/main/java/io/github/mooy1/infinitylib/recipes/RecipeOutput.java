package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class RecipeOutput {

    @Getter
    private final ItemStack output;
    private final AbstractRecipe recipe;

    @Nonnull
    public ItemStack cloneOutput() {
        return this.output.clone();
    }

    @Nonnull
    public ItemStack cloneAndConsume() {
        this.recipe.consumeRecipe();
        return cloneOutput();
    }

    public void consumeRecipe() {
        this.recipe.consumeRecipe();
    }

}
