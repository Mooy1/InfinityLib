package io.github.mooy1.infinitylib.recipes;

import org.bukkit.inventory.ItemStack;

public final class ShapedRecipes extends AbstractRecipes<ItemStack[], ShapedRecipe> {

    public ShapedRecipes() {
        super(ShapedRecipe::new);
    }

}
