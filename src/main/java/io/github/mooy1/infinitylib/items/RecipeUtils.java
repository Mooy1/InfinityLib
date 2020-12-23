package io.github.mooy1.infinitylib.items;

import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.player.MessageUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public final class RecipeUtils {
    
    public static MultiFilter getRecipeFilter(ShapedRecipe recipe) {
        ItemFilter[] array = new ItemFilter[9];
        for (int i = 0 ; i < recipe.getShape().length ; i++) {
            for (int j = 0 ; j < recipe.getShape()[i].length() ; j++) {
                ItemStack item = recipe.getIngredientMap().get(recipe.getShape()[i].charAt(j));
                if (item != null) {
                    array[j + i * 3] = new ItemFilter(item);
                }
                MessageUtils.broadcast("    " + i + "        " + item);
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
            MessageUtils.broadcast(" " + item);
        }
        return new MultiFilter(array);
    }

}
