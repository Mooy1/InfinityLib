package io.github.mooy1.infinitylib.recipes;

import lombok.RequiredArgsConstructor;

import io.github.mooy1.infinitylib.items.CachedItemStack;

@RequiredArgsConstructor
public abstract class AbstractRecipe {

    private final CachedItemStack[] input;
    private AbstractRecipe recipe;

    @Override
    public abstract int hashCode();

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof AbstractRecipe && equals(this.recipe = (AbstractRecipe) obj);
    }

    protected abstract boolean equals(AbstractRecipe recipe);
    
    final void consumeRecipe() {
        consume(this.recipe);
    }

    protected abstract void consume(AbstractRecipe recipe);

    protected CachedItemStack[] getInput() {
        return this.input;
    }

}
