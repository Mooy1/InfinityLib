package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public abstract class AbstractRecipe {

    private final ItemStack[] rawInput;
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

    protected final ItemStack[] getRawInput() {
        return this.rawInput;
    }

    final ItemStack[] getRecipeInput() {
        return this.matchingRecipe.rawInput;
    }

    final void consumeMatchingRecipe() {
        this.matchingRecipe.consume(this);
    }

}
