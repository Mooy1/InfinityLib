package io.github.mooy1.infinitylib.recipes;

import org.bukkit.inventory.ItemStack;

public class ShapelessRecipes extends AbstractRecipes<ItemStack[], ShapelessRecipe> {

    public ShapelessRecipes() {
        super(ShapelessRecipe::new);
    }

}
