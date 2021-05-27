package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.CachedItemStack;

public final class BasicRecipeMap extends RecipeMap<CachedItemStack[]> {

    @Nonnull
    @Override
    protected RecipeInput create(@Nonnull ItemStack[] inputs) {
        return new RecipeInput(inputs, CachedItemStack.ofArray(inputs));
    }

    @Override
    protected void consume(@Nonnull RecipeInput input, @Nonnull RecipeInput recipe) {
        CachedItemStack[] inputs = input.getData();
        CachedItemStack[] recipes = recipe.getData();
        for (int i = 0 ; i < recipe.getData().length ; i++) {
            CachedItemStack in = inputs[i];
            if (in != null) {
                in.setAmount(in.getAmount() - recipes[i].getAmount());
            }
        }
    }

    @Override
    protected boolean equals(@Nonnull CachedItemStack[] input, @Nonnull CachedItemStack[] recipe) {
        for (int i = 0 ; i < input.length ; i++) {
            CachedItemStack in = input[i];
            CachedItemStack re = recipe[i];
            if (in == null) {
                if (re != null) {
                    return false;
                }
            } else if (re != null && !in.getIDorType().equals(re.getIDorType())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int hashCode(@Nonnull ItemStack[] input, @Nonnull CachedItemStack[] data) {
        int hash = 0;
        for (CachedItemStack item : data) {
            if (item != null) {
                hash += item.getIDorType().hashCode();
            }
        }
        return hash;
    }

}
