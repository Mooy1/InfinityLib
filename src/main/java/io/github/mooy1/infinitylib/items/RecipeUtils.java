package io.github.mooy1.infinitylib.items;

import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public final class RecipeUtils {
    
    public static MultiFilter getRecipeFilter(ShapedRecipe recipe) {
        ItemFilter[] array = new ItemFilter[9];
        for (int i = 0 ; i < recipe.getShape().length ; i++) {
            for (int j = 0 ; j < recipe.getShape()[i].length() ; j++) {
                array[(i + 1) * (j + 1) - 1] = new ItemFilter(recipe.getIngredientMap().get(recipe.getShape()[i].charAt(j))); 
            }
        }
        return new MultiFilter(array);
    }

    public static MultiFilter getRecipeFilter(ShapelessRecipe recipe) {
        ItemFilter[] array = new ItemFilter[9];
        for (int i = 0 ; i < recipe.getIngredientList().size() ; i++) {
            array[i] = new ItemFilter(recipe.getIngredientList().get(i));
        }
        return new MultiFilter(array);
    }

}
