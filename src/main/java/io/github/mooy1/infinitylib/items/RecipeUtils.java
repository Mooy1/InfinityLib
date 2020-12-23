package io.github.mooy1.infinitylib.items;

import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public final class RecipeUtils {
    
    public static MultiFilter getRecipeFilter(ShapedRecipe recipe) {
        ItemFilter[] array = new ItemFilter[9];
        for (int row = 0 ; row < recipe.getShape().length ; row++) {
            String line = recipe.getShape()[row];
            
            for (int column = 0 ; column < line.length() ; column++) {
                ItemStack item = recipe.getIngredientMap().get(line.charAt(column));
                
                if (item != null) {
                    array[(row * 3) + column] = new ItemFilter(item);
                }
            }
        }
        return new MultiFilter(array);
    }

    public static MultiFilter getRecipeFilter(ShapelessRecipe recipe) {
        ItemFilter[] array = new ItemFilter[9];
        for (int i = 0 ; i < recipe.getIngredientList().size() ; i++) {
            ItemStack item = recipe.getIngredientList().get(i);
            if (item != null) {
                array[i] = new ItemFilter(item);
            }
        }
        return new MultiFilter(array);
    }

}
