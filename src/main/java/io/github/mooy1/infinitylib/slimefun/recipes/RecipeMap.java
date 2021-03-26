package io.github.mooy1.infinitylib.slimefun.recipes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class RecipeMap<I extends RecipeInput<O>, O> {
    
    private final Map<I, O> recipes = new HashMap<>();
    
    public void add(I input, O output) {
        input.affectOutput(output);
        this.recipes.put(input, output);
    }
    
    public O get(I input) {
        return this.recipes.get(input);
    }
    
    public Collection<Map.Entry<I, O>> entries() {
        return this.recipes.entrySet();
    }
    
    public Collection<I> inputs() {
        return this.recipes.keySet();
    }

    public Collection<O> outputs() {
        return this.recipes.values();
    }
    
    public int size() {
        return this.recipes.size();
    }
    
}
