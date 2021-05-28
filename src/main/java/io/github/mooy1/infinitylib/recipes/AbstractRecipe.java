package io.github.mooy1.infinitylib.recipes;

import lombok.RequiredArgsConstructor;

import io.github.mooy1.infinitylib.items.FastItemStack;

@RequiredArgsConstructor
public abstract class AbstractRecipe {

    private final FastItemStack[] rawInput;
    private AbstractRecipe matchingRecipe;

    @Override
    public abstract int hashCode();

    protected abstract boolean matches();

    protected abstract void consume();

    @Override
    public final boolean equals(Object recipe) {
        if (recipe instanceof AbstractRecipe) {
            this.matchingRecipe = (AbstractRecipe) recipe;
            return matches();
        }
        return false;
    }

    public final FastItemStack[] getRawInput() {
        return this.rawInput;
    }

    public final AbstractRecipe getMatchingRecipe() {
        return this.matchingRecipe;
    }

}
