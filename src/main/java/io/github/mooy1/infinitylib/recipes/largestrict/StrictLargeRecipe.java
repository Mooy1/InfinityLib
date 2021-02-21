package io.github.mooy1.infinitylib.recipes.largestrict;

import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

/**
 * A large recipe which stores a string of all ids/materials and amounts
 */
final class StrictLargeRecipe {

    private final String string;
    final int[] amounts;

    StrictLargeRecipe(ItemStack[] recipe, int[] amounts) {
        StringBuilder builder = new StringBuilder();
        for (ItemStack item : recipe) {
            if (item != null) {
                builder.append(StackUtils.getIDorType(item));
            }
            builder.append(';');
        }
        this.string = builder.toString();
        this.amounts = amounts;
    }

    StrictLargeRecipe(ItemStack[] recipe) {
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

    StrictLargeRecipe(BlockMenu menu, int[] slots) {
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
        if (!(obj instanceof StrictLargeRecipe)) return false;
        StrictLargeRecipe recipe = (StrictLargeRecipe) obj;
        for (int i = 0 ; i < this.amounts.length ; i++) {
            if (recipe.amounts[i] > this.amounts[i]) {
                return false;
            }
        }
        return recipe.string.equals(this.string);
    }

}
