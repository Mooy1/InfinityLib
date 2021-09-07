package io.github.mooy1.infinitylib.machines;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemStackSnapshot;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

@Getter
public final class CraftingBlockRecipe {

    private final ItemStack[] inputs;
    final ItemStack output;
    final SlimefunItem item;

    CraftingBlockRecipe(ItemStack output, ItemStack[] inputs) {
        this.output = output;
        this.inputs = ItemStackSnapshot.wrapArray(inputs);
        this.item = SlimefunItem.getByItem(output);
    }

    boolean check(ItemStackSnapshot[] input) {
        for (int i = 0; i < inputs.length; i++) {
            if (!SlimefunUtils.isItemSimilar(input[i], inputs[i], false, true)) {
                return false;
            }
        }
        return true;
    }

    boolean check(Player p) {
        return item == null || item.canUse(p, true);
    }

    void consume(ItemStack[] input) {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] != null) {
                ItemUtils.consumeItem(input[i], inputs[i].getAmount(), true);
            }
        }
    }
    
}
