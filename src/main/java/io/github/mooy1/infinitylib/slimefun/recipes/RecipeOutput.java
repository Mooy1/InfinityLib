package io.github.mooy1.infinitylib.slimefun.recipes;

public abstract class RecipeOutput<I extends RecipeInput> {

    protected abstract void accept(I input);
    
}
