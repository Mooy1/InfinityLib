package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;

import io.github.mooy1.infinitylib.items.FastItemStack;

@RequiredArgsConstructor
public abstract class AbstractRecipe {

    private final FastItemStack[] rawInput;
    private AbstractRecipe matchingRecipe;

    @Override
    public final boolean equals(Object recipe) {
        if (recipe instanceof AbstractRecipe) {
            return (this.matchingRecipe = (AbstractRecipe) recipe).matches(this);
        }
        return false;
    }

    @Override
    public abstract int hashCode();

    protected abstract boolean matches(@Nonnull AbstractRecipe input);

    protected abstract void consume(@Nonnull AbstractRecipe input);

    protected final FastItemStack[] getRawInput() {
        return this.rawInput;
    }

    final FastItemStack[] getRecipeInput() {
        return this.matchingRecipe.rawInput;
    }

    final void consumeMatchingRecipe() {
        this.matchingRecipe.consume(this);
    }

}
