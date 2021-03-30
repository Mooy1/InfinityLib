package io.github.mooy1.infinitylib.slimefun.recipes;

public final class AdvancedRecipeMap<I extends RecipeInput, O extends RecipeOutput<I>> extends RecipeMap<I, O> {
    
    @Override
    public void put(I input, O output) {
        output.accept(input);
        this.recipes.put(input, output);
    }

}
