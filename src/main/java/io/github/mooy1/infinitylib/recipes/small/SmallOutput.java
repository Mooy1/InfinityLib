package io.github.mooy1.infinitylib.recipes.small;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import io.github.mooy1.infinitylib.items.FastItemStack;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class SmallOutput<O> {

    @Getter
    private final O output;
    private final SmallRecipe input;

    public FastItemStack getOriginalInput() {
        return this.input.getInput();
    }

    public FastItemStack getRecipeInput() {
        return this.input.getMatchingRecipe().getInput();
    }

    public void consumeInput() {
        this.input.consume();
    }

    public O getAndConsume() {
        consumeInput();
        return this.output;
    }

}
