package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;

final class ShapelessRecipe extends AbstractRecipe<ItemStack[], ShapelessRecipe> {

    @Getter
    private final int hashCode;
    private final Map<String, Integer> amounts = new HashMap<>(4);
    private final Map<String, Set<Integer>> items = new HashMap<>(4);

    public ShapelessRecipe(ItemStack[] input) {
        super(input);
        int hash = 0;
        for (int i = 0; i < input.length; i++) {
            ItemStack item = input[i];
            String string = getString(item);
            if (string != AIR) {
                this.amounts.put(string, item.getAmount());
                this.items.computeIfAbsent(string, k -> new HashSet<>(2)).add(i);
            }
            hash += string.hashCode();
        }
        this.hashCode = hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShapelessRecipe) {
            ShapelessRecipe recipe = (ShapelessRecipe) obj;
            for (Map.Entry<String, Integer> entry : this.amounts.entrySet()) {
                if (entry.getValue() < recipe.amounts.getOrDefault(entry.getKey(), 0)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void consumeRecipe() {
        for (Map.Entry<String, Integer> entry : this.recipe.amounts.entrySet()) {
            int consume = entry.getValue();
            for (Integer index : this.items.get(entry.getKey())) {
                ItemStack item = this.original[index];
                int amount = item.getAmount() - consume;
                if (amount < 0) {
                    consume = -amount;
                    item.setAmount(0);
                } else {
                    item.setAmount(amount);
                    break;
                }
            }
        }
    }

}
