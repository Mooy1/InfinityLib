package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;

/**
 * A utility class for testing if items fit a 'filter' for each other
 * 
 * @author Mooy1
 * 
 */
public class ItemFilter {
    
    @Getter
    protected final int amount;
    protected final String string;
    protected final FilterType equalsType;
    
    public ItemFilter(@Nonnull ItemStack stack, @Nonnull FilterType equalsType) {
        this.string = StackUtils.getIDorType(stack);
        this.amount = stack.getAmount();
        this.equalsType = equalsType;
    }
    
    public ItemFilter(@Nonnull Material material, @Range(from = 1, to = 64) int amount, @Nonnull FilterType equalsType) {
        this(new ItemStack(material, amount), equalsType);
    }

    public ItemFilter(@Nonnull Material material, @Nonnull FilterType equalsType) {
        this(material, 1, equalsType);
    }

    /**
     * Checks if this filter will fit another filter
     */
    public boolean fits(@Nonnull ItemFilter input, @Nonnull FilterType type) {
        return type.filter(this.amount, input.getAmount()) && this.string.equals(input.string);
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
        return ((ItemFilter) obj).fits(this);
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }
    
}
