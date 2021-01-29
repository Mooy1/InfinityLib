package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A utility class to be used over ItemFilter arrays
 *
 * @author Mooy1
 *
 */
public class MultiFilter {
    
    @Getter
    protected final int[] amounts;
    protected final String string;
    protected final FilterType equalsType;

    public MultiFilter(@Nonnull FilterType equalsType, @Nonnull ItemStack... stacks) { 
        StringBuilder builder = new StringBuilder();
        int[] amounts = new int[stacks.length];
        for (int i = 0 ; i < stacks.length ; i++) {
            ItemStack stack = stacks[i];
            if (stack != null) {
                builder.append(StackUtils.getIDorType(stack));
                amounts[i] = stack.getAmount();
            }
            builder.append("-");
        }
        this.string = builder.toString();
        this.amounts = amounts;
        this.equalsType = equalsType;
    }

    /**
     * Checks if this filter will fit another filter
     */
    public boolean fits(@Nonnull MultiFilter input, @Nonnull FilterType type) {
        return type.filter(this.amounts, input.getAmounts()) && this.string.equals(input.string);
    }

    /**
     * Checks if this filter will fit another filter using this filters equalsType
     */
    public boolean fits(@Nonnull MultiFilter input) {
        return fits(input, this.equalsType);
    }
    
    /**
     * creates a multi filter from a menu using given slots and size
     */
    @Nonnull
    public static MultiFilter fromMenu(@Nonnull FilterType type, @Nonnull BlockMenu menu, @Nonnull int[] slots) {
        ItemStack[] array = new ItemStack[slots.length];
        for (int i = 0 ; i < slots.length ; i++) {
            array[i] = menu.getItemInSlot(slots[i]);
        }
        return new MultiFilter(type, array);
    }
    
    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MultiFilter && ((MultiFilter) obj).fits(this);
    }

}
