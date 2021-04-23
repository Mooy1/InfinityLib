package io.github.mooy1.infinitylib.slimefun.recipes.inputs;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * A large recipe which stores a string of all ids/materials and amounts
 */
public class StrictMultiInput {

    private final String string;
    @Getter
    private final int[] amounts;

    public StrictMultiInput(ItemStack[] recipe) {
        StringBuilder builder = new StringBuilder();
        int[] amounts = new int[recipe.length];
        for (int i = 0 ; i < recipe.length ; i++) {
            ItemStack item = recipe[i];
            if (item != null) {
                builder.append(StackUtils.getIDorType(item));
                amounts[i] = item.getAmount();
            }
            builder.append(';');
        }
        this.string = builder.toString();
        this.amounts = amounts;
    }

    public StrictMultiInput(BlockMenu menu, int[] slots) {
        StringBuilder builder = new StringBuilder();
        int[] amounts = new int[slots.length];
        for (int i = 0 ; i < slots.length ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                builder.append(StackUtils.getIDorType(item));
                amounts[i] = item.getAmount();
            }
            builder.append(';');
        }
        this.string = builder.toString();
        this.amounts = amounts;
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StrictMultiInput)) {
            return false;
        }
        StrictMultiInput recipe = (StrictMultiInput) obj;
        if (recipe.amounts.length != this.amounts.length) {
            return false;
        }
        for (int i = 0 ; i < this.amounts.length ; i++) {
            if (recipe.amounts[i] > this.amounts[i]) {
                return false;
            }
        }
        return recipe.string.equals(this.string);
    }

}
