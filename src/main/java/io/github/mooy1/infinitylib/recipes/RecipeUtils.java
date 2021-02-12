package io.github.mooy1.infinitylib.recipes;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;

/**
 * Utility class to convert vanilla recipes into item stack arrays of size 9
 * 
 * @author Mooy1
 */
@UtilityClass
public final class RecipeUtils {

    @Nonnull
    public static ItemStack[] fromShapeless(@Nonnull ShapelessRecipe recipe) {
        ItemStack[] arr = new ItemStack[9];
        for (int i = 0 ; i < Math.min(9, recipe.getIngredientList().size()) ; i++) {
            arr[i] = recipe.getIngredientList().get(i);
        }
        return arr;
    }
    
    @Nonnull
    public static ItemStack[] fromShaped(@Nonnull ShapedRecipe recipe) {
        ItemStack[] arr = new ItemStack[9];
        for (int i = 0 ; i < recipe.getShape().length ; i++) {
            for (int j = 0 ; j < recipe.getShape()[0].length() ; j++) {
                arr[i * 3 + j] = recipe.getIngredientMap().get(recipe.getShape()[i].charAt(j));
            }
        }
        return arr;
    }
    
}
