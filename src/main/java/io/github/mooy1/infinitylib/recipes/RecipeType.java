package io.github.mooy1.infinitylib.recipes;

import java.util.function.Function;

import org.bukkit.inventory.ItemStack;

public interface RecipeType extends Function<ItemStack[], AbstractRecipe> {

    RecipeType SHAPELESS = ShapelessRecipe::new;
    RecipeType SHAPED = ShapedRecipe::new;

}
