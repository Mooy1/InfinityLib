package io.github.mooy1.infinitylib.slimefun.recipes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class RecipeMap<I extends RecipeInput, O> {
    
    final Map<I, O> recipes = new HashMap<>();
    
    RecipeMap() {
        
    }
    
    public abstract void put(I input, O output);
    
    public final O get(I input) {
        return this.recipes.get(input);
    }
    
    public final Collection<Map.Entry<I, O>> entries() {
        return this.recipes.entrySet();
    }
    
    public final Collection<I> inputs() {
        return this.recipes.keySet();
    }

    public final Collection<O> outputs() {
        return this.recipes.values();
    }
    
    public final int size() {
        return this.recipes.size();
    }
    
}
