package io.github.mooy1.infinitylib.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for classes which hold a map
 * 
 * @author Mooy1
 */
public abstract class MapHolder<K, V> {
  
    protected final Map<K, V> map = new HashMap<>();
    
    public final int size() {
        return this.map.size();
    }
    
}
