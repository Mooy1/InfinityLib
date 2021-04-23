package io.github.mooy1.infinitylib.slimefun.recipes.inputs;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class MultiInput {
    
    private final String string;

    public MultiInput(ItemStack[] recipe) {
        StringBuilder builder = new StringBuilder();
        
        for (ItemStack itemStack : recipe) {
            if (itemStack != null) {
                builder.append(StackUtils.getIDorType(itemStack)).append(';');
            }
        }

        this.string = builder.toString();
    }

    public MultiInput(BlockMenu menu, int[] slots) {
        StringBuilder builder = new StringBuilder();

        for (int slot : slots) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null) {
                builder.append(StackUtils.getIDorType(item)).append(';');
            }
        }

        this.string = builder.toString();
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MultiInput && ((MultiInput) obj).string.equals(this.string);
    }
    
}
