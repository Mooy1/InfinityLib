package io.github.mooy1.infinitylib.filter;

import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.math.MathUtils;
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
    public static RecipeFilter[] fromRecipe(@Nonnull ShapedRecipe recipe) {

        int width = recipe.getShape().length;
        int height = recipe.getShape()[0].length();

        int x = 4 - width;
        int y = 4 - height;

        RecipeFilter[] filters = new RecipeFilter[x * y];

        while (x-- > 0) {
            while (y-- > 0) {
                ItemStack[] arr = new ItemStack[9];
                for (int a = 0 ; a < width ; a++) {
                    for (int b = 0 ; b < height ; b++) {
                        arr[3 * (a + x) + b + y] = recipe.getIngredientMap().get(recipe.getShape()[a].charAt(b));
                    }
                }
                filters[(x + 1) * (y + 1) - 1] = new RecipeFilter(FilterType.IGNORE_AMOUNT, arr);
            }
            y = 4 - height;
        }
        
        return filters;
    }

    @Nonnull
    public static RecipeFilter[] fromRecipe(@Nonnull ShapelessRecipe recipe) {
        ItemStack[][] combos = MathUtils.combinations(recipe.getIngredientList().toArray(new ItemStack[0]));
        RecipeFilter[] filters = new RecipeFilter[combos.length];
        for (int i = 0 ; i < combos.length ; i++) {
            filters[i] = new RecipeFilter(FilterType.IGNORE_AMOUNT, combos[i]);
        }
        return filters;
    }
    
}
