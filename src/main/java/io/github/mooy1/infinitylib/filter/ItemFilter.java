package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
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
    @Getter
    private final String string;
    @Getter
    private final ItemStack item;
    private final FilterType equalsType;
    
    public ItemFilter(@Nullable ItemStack stack, @Nonnull FilterType equalsType) {
        if (stack == null) {
            this.string = "";
            this.amount = 0;
        } else {
            String id = StackUtils.getID(stack);
            if (id == null) {
                this.string = String.valueOf(stack.getType().ordinal());
            } else {
                this.string = id;
            }
            this.amount = stack.getAmount();
        }
        this.item = stack;
        this.equalsType = equalsType;
    }
    
    public ItemFilter(@Nonnull Material material, int amount, @Nonnull FilterType equalsType) {
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
