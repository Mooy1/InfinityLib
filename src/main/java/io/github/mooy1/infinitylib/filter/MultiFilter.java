package io.github.mooy1.infinitylib.filter;

import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

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
    @Nonnull
    private final ItemFilter[] filters;
    
    public MultiFilter(@Nonnull ItemFilter... filters) {
        int hashcode = filters.length;
        int[] amounts = new int[filters.length];
        for (int i = 0 ; i < filters.length ; i++) {
            ItemFilter filter = filters[i];
            if (filter != null) {
                amounts[i] = filter.getAmount();
            }
            hashcode += i + Objects.hashCode(filter);
        }
        this.hashcode = hashcode;
        this.amounts = amounts;
        this.filters = filters;
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
    public static MultiFilter fromMenu(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        ItemFilter[] array = new ItemFilter[slots.length];
        
        for (int i = 0 ; i < slots.length ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                array[i] = new ItemFilter(item);
            }
        }

        return new MultiFilter(array);
    }
    
    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MultiFilter)) return false;
        return matches((MultiFilter) obj, FilterType.EQUAL_AMOUNT);
    }

}
