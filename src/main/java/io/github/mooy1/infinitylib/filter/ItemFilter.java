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
    private final int hashcode;
    @Getter
    private final ItemStack item;
    private final FilterType equalsType;
    
    public ItemFilter(@Nonnull ItemStack stack, @Nonnull FilterType equalsType) {
        String id = StackUtils.getItemID(stack, false);
        this.hashcode = id != null ? id.hashCode() : stack.getType().ordinal() * 31; // TESTING
        this.amount = stack.getAmount();
        this.item = stack;
        this.equalsType = equalsType;
    }
    
    public ItemFilter(@Nonnull ItemStack stack) {
        this(stack, FilterType.EQUAL_AMOUNT);
    }

    public ItemFilter(@Nonnull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemFilter(@Nonnull Material material) {
        this(material, 1);
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
        return matches((ItemFilter) obj, this.equalsType);
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }
    
}
