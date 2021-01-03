package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * A utility class to be used over ItemFilter arrays
 *
 * @author Mooy1
 *
 */
public class MultiFilter {
    
    @Getter
    private final String string;
    @Getter
    @Nonnull
    private final int[] amounts;
    @Getter
    @Nonnull
    private final ItemStack[] stacks;
    private final FilterType equalsType;

    public MultiFilter(@Nonnull FilterType equalsType, @Nonnull ItemStack... stacks) { 
        StringBuilder builder = new StringBuilder();
        int[] amounts = new int[stacks.length];
        for (int i = 0 ; i < stacks.length ; i++) {
            ItemStack stack = stacks[i];
            if (stack != null) {
                String id = StackUtils.getItemID(stack, false);
                if (id == null) {
                    builder.append(stack.getType().ordinal());
                } else {
                    builder.append(id);
                }
                amounts[i] = stack.getAmount();
            }
            builder.append("|");
        }
        this.string = builder.toString();
        this.amounts = amounts;
        this.stacks = stacks;
        this.equalsType = equalsType;
    }

    public static MultiFilter fromRecipe(ShapedRecipe recipe) {
        ItemStack[] array = new ItemStack[9];
        for (int row = 0 ; row < recipe.getShape().length ; row++) {
            String line = recipe.getShape()[row];
            for (int column = 0 ; column < line.length() ; column++) {
                array[(row * 3) + column] = recipe.getIngredientMap().get(line.charAt(column));
            }
        }
        return new MultiFilter(FilterType.MIN_AMOUNT, array);
    }

    public static MultiFilter fromRecipe(ShapelessRecipe recipe) {
        ItemStack[] array = new ItemStack[9];
        for (int i = 0 ; i < recipe.getIngredientList().size() ; i++) {
            array[i] = recipe.getIngredientList().get(i);
        }
        return new MultiFilter(FilterType.MIN_AMOUNT, array);
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
     * gets the index of this array that matches the given filter
     */
    public int[] getTransportSlot(@Nonnull ItemStack input, int[] possible) {
        ItemFilter filter = new ItemFilter(input, FilterType.IGNORE_AMOUNT);
        List<Integer> list = new ArrayList<>(4);
        for (int i = 0 ; i < this.stacks.length ; i++) {
            ItemStack stack = this.stacks[i];
            if (stack != null && filter.fits(new ItemFilter(stack, FilterType.IGNORE_AMOUNT))) {
                list.add(i);
            }
        }
        int[] slots = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++) {
            slots[i] = possible[list.get(i)];
        }
        return slots;
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
        if (!(obj instanceof MultiFilter)) return false;
        return fits((MultiFilter) obj, this.equalsType);
    }

}
