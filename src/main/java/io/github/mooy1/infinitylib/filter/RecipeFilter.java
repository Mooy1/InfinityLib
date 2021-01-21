package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;

/**
 * A multi filter which stores additional data for finding cargo transport slots in a recipe
 */
public class RecipeFilter extends MultiFilter {

    private final String[] strings;
    
    public RecipeFilter(@Nonnull FilterType equalsType, @Nonnull ItemStack... stacks) {
        super(equalsType, stacks);
        this.strings = super.string.split("-");
    }


    /**
     * gets the index of this array that matches the given filter
     */
    @Nonnull
    public int[] getTransportSlot(@Nonnull ItemStack input, @Nonnull int[] possible) {
        String id = StackUtils.getIDorType(input);
        int[] slots = new int[0];
        int slot = 0;
        for (int i = 0 ; i < this.strings.length ; i++) {
            if (id.equals(this.strings[i])) {
                int[] array = new int[slot + 1];
                System.arraycopy(slots, 0, array, 0, slot);
                array[slot++] = possible[i];
                slots = array;
            }
        }
        return slots;
    }

    @Nonnull
    public static RecipeFilter fromRecipe(@Nonnull ShapedRecipe recipe, @Nonnull FilterType type) {
        ItemStack[] array = new ItemStack[9];
        for (int row = 0 ; row < recipe.getShape().length ; row++) {
            String line = recipe.getShape()[row];
            for (int column = 0 ; column < line.length() ; column++) {
                array[row * 3 + column] = recipe.getIngredientMap().get(line.charAt(column));
            }
        }
        return new RecipeFilter(type, array);
    }

    @Nonnull
    public static RecipeFilter fromRecipe(@Nonnull ShapelessRecipe recipe, @Nonnull FilterType type) {
        ItemStack[] array = new ItemStack[9];
        for (int i = 0 ; i < recipe.getIngredientList().size() ; i++) {
            array[i] = recipe.getIngredientList().get(i);
        }
        return new RecipeFilter(type, array);
    }
    
}
