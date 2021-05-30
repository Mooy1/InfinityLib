package io.github.mooy1.infinitylib.recipes;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import io.github.mooy1.infinitylib.items.FastItemStack;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class RecipeOutput<O> {

    @Getter
    private final O output;
    private final AbstractRecipe input;

    public FastItemStack[] getOriginalInput() {
        return this.input.getRawInput();
    }

    public FastItemStack[] getRecipeInput() {
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
