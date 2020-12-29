package io.github.mooy1.infinitylib.filter;

import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A utility class to be used over ItemFilter arrays
 *
 * @author Mooy1
 *
 */
public class MultiFilter {
    
    private final int hashcode;
    @Getter
    @Nonnull
    private final int[] amounts;
    @Getter
    @Nonnull
    private final ItemFilter[] filters;
    private final FilterType equalsType;
    
    public MultiFilter(@Nonnull FilterType equalsType, @Nonnull ItemFilter... filters) { 
        int hashcode = filters.length;
        int[] amounts = new int[filters.length];
        for (int i = 0 ; i < filters.length ; i++) {
            ItemFilter filter = filters[i];
            if (filter != null) {
                amounts[i] = filter.getAmount();
            }
            hashcode += filter != null ? filter.hashCode() >> i : i * i; // NEED TO FIX TO CORRECTLY USE ORDER
        }
        this.hashcode = hashcode;
        this.amounts = amounts;
        this.filters = filters;
        this.equalsType = equalsType;
    }
    
    public static MultiFilter fromStacks(@Nonnull FilterType type, ItemStack... stacks) {
        ItemFilter[] filters = new ItemFilter[stacks.length];
        for (int i = 0 ; i < stacks.length ; i++) {
            ItemStack stack = stacks[i];
            if (stack != null) {
                filters[i] = new ItemFilter(stack, type);
            }
        }
        return new MultiFilter(type, filters);
    }
    
    public int size() {
        return this.amounts.length;
    }

    /**
     * gets the index of this array that matches the given filter
     */
    public int indexOf(@Nonnull ItemFilter match, @Nonnull FilterType type) {
        for (int i = 0 ; i < size() ; i++) {
            ItemFilter filter = this.filters[i];
            if (filter != null && filter.matches(match, type)) return i;
        }
        return -1;
    }

    /**
     * Checks if this filter will fit another filter, returns true if both are null
     */
    public static boolean matches(@Nullable MultiFilter recipe, @Nullable MultiFilter input, @Nonnull FilterType type) {
        if ((recipe == null) != (input == null) ) return false;
        if (recipe == null) return true;
        return recipe.matches(input, type);
    }

    /**
     * Checks if this filter will fit another filter
     */
    public boolean matches(@Nonnull MultiFilter input, @Nonnull FilterType type) {
        return type.filter(this.amounts, input.getAmounts()) && this.hashcode == input.hashCode();
    }

    /**
     * creates a multi filter from a menu using given slots and size
     */
    @Nonnull
    public static MultiFilter fromMenu(@Nonnull FilterType type, @Nonnull BlockMenu menu, @Nonnull int[] slots) {
        ItemFilter[] array = new ItemFilter[slots.length];
        
        for (int i = 0 ; i < slots.length ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                array[i] = new ItemFilter(item, type);
            }
        }

        return new MultiFilter(type, array);
    }
    
    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MultiFilter)) return false;
        return matches((MultiFilter) obj, this.equalsType);
    }

}
