package io.github.mooy1.infinitylib.recipes;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.inventory.ItemStack;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class RecipeOutput<O> {

    @Getter
    private final O output;
    private final AbstractRecipe input;

    public ItemStack[] getOriginalInput() {
        return this.input.getRawInput();
    }

    public ItemStack[] getRecipeInput() {
        return this.input.getRecipeInput();
    }

    public void consumeInput() {
        this.input.consumeMatchingRecipe();
    }

    public O getAndConsume() {
        consumeInput();
        return this.output;
    }

}
