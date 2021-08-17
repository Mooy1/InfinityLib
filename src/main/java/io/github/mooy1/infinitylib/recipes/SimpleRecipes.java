package io.github.mooy1.infinitylib.recipes;

import org.bukkit.inventory.ItemStack;

public final class SimpleRecipes extends AbstractRecipes<ItemStack, SimpleRecipe> {

    public SimpleRecipes() {
        super(SimpleRecipe::new);
    }

}
