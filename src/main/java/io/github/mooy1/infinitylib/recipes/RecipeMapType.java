package io.github.mooy1.infinitylib.recipes;

import java.util.function.Function;

import org.bukkit.inventory.ItemStack;

public interface RecipeMapType extends Function<ItemStack[], AbstractRecipe> {

    RecipeMapType SHAPELESS = ShapelessRecipe::new;
    RecipeMapType SHAPED = ShapedRecipe::new;

}
