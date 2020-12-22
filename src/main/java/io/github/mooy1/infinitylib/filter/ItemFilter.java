package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import lombok.Getter;
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
    private final int hashcode;
    @Getter
    private final ItemStack item;
    
    public ItemFilter(@Nonnull ItemStack stack) {
        String id = StackUtils.getItemID(stack, false);
        this.hashcode = id != null ? id.hashCode() : stack.getType().hashCode(); // TESTING
        this.amount = stack.getAmount();
        this.item = stack;
    }
    
    /**
     * Checks if this filter will fit another filter, returns true if both are null
     */
    public static boolean matches(@Nullable ItemFilter recipe, @Nullable ItemFilter input, @Nonnull FilterType type) {
        if ((recipe == null) != (input == null)) return false;
        if (recipe == null) return true;
        return recipe.matches(input, type);
    }

    /**
     * Checks if this filter will fit another filter
     */
    public boolean matches(@Nonnull ItemFilter input, @Nonnull FilterType type) {
        return type.filter(this.amount, input.getAmount()) && this.hashcode == input.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemFilter)) return false;
        return matches((ItemFilter) obj, FilterType.EQUAL_AMOUNT);
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }
    
}
