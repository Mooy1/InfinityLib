package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.FastItemStack;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractRecipe {

    private final FastItemStack[] input;
    private AbstractRecipe recipe;
    private ItemStack output;

    @Override
    public abstract int hashCode();

    protected abstract boolean equals(@Nonnull AbstractRecipe recipe);

    protected abstract void consume(@Nonnull AbstractRecipe recipe);

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof AbstractRecipe && equals(this.recipe = (AbstractRecipe) obj);
    }
    
    public final void consumeInput() {
        consume(this.recipe);
    }

    public final FastItemStack[] getInput() {
        return this.input;
    }

    public final ItemStack getOutput() {
        return this.recipe.output;
    }

    public final ItemStack cloneOutput() {
        return getOutput().clone();
    }

    final void setOutput(@Nonnull ItemStack output) {
        this.output = output;
    }

}
