package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for classes which hold a map
 * 
 * @author Mooy1
 */
public abstract class AbstractRecipeMap<K, V> {
  
    protected final Map<K, V> recipes = new HashMap<>();
    
    public final int size() {
        return this.recipes.size();
    }
    
}
