package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.Map;

import io.github.mooy1.infinitylib.items.FastItemStack;

public final class ShapelessRecipe extends AbstractRecipe {

    private final Map<String, Integer> map = new HashMap<>(8);

    public ShapelessRecipe(FastItemStack[] input) {
        super(input);
        for (FastItemStack item : input) {
            if (item != null) {
                this.map.compute(item.getIDorType(),
                        (k, v) -> v == null ? item.getAmount() : v + item.getAmount());
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (String s : this.map.keySet()) {
            hash += s.hashCode();
        }
        return hash;
    }

    @Override
    protected boolean matches() {
        for (Map.Entry<String, Integer> entry : ((ShapelessRecipe) getMatchingRecipe()).map.entrySet()) {
            if (this.map.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void consume() {
        for (Map.Entry<String, Integer> entry : ((ShapelessRecipe) getMatchingRecipe()).map.entrySet()) {
            int rem = entry.getValue();

            for (FastItemStack item : getRawInput()) {

                if (item != null && item.getIDorType().equals(entry.getKey())) {

                    int amt = item.getAmount();

                    if (amt >= rem) {
                        item.setAmount(amt - rem);
                        break;
                    }

                    rem -= amt;
                }
            }
        }
    }

}
