package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A utility class for testing if items fit a 'filter' for each other
 * 
 * @author Mooy1
 * 
 */
public class ItemFilter {
    
    @Getter
    private final int amount;
    private final String string;
    @Getter
    private final ItemStack item;
    private final FilterType equalsType;
    
    public ItemFilter(@Nonnull ItemStack stack, @Nonnull FilterType equalsType) {
        String id = StackUtils.getItemID(stack, false);
        if (id == null) {
            this.string = String.valueOf(stack.getType().ordinal());
        } else {
            this.string = id;
        }
        this.amount = stack.getAmount();
        this.item = stack;
        this.equalsType = equalsType;
    }
    
    public ItemFilter(@Nonnull Material material, int amount, @Nonnull FilterType equalsType) {
        this(new ItemStack(material, amount), equalsType);
    }

    public ItemFilter(@Nonnull Material material, @Nonnull FilterType equalsType) {
        this(material, 1, equalsType);
    }
    
    @Nullable
    public static ItemFilter get(@Nullable ItemStack stack, @Nonnull FilterType equalsType) {
        return stack != null ? new ItemFilter(stack, equalsType) : null;
    }

    /**
     * Checks if this filter will fit another filter
     */
    public boolean fits(@Nonnull ItemFilter input, @Nonnull FilterType type) {
        return type.filter(this.amount, input.getAmount()) && this.string.hashCode() == input.string.hashCode();
    }

    /**
     * Checks if this filter will fit another filter using this filters equalsType
     */
    public boolean fits(@Nonnull ItemFilter input) {
        return fits(input, this.equalsType);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemFilter)) return false;
        return fits((ItemFilter) obj, this.equalsType);
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }
    
}
