package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.CachedItemStack;

@RequiredArgsConstructor
public final class RecipeMap {

    private final Function<CachedItemStack[], AbstractRecipe> recipeConstructor;

    private final Map<AbstractRecipe, ItemStack> map = new HashMap<>();

    public void put(@Nonnull ItemStack[] recipe, @Nonnull ItemStack output) {
        this.map.put(this.recipeConstructor.apply(CachedItemStack.ofArray(recipe)), output);
    }

    @Nullable
    public final RecipeOutput get(@Nonnull ItemStack[] input) {
        AbstractRecipe recipe = this.recipeConstructor.apply(CachedItemStack.ofArray(input));
        ItemStack output = this.map.get(recipe);
        if (output != null) {
            return new RecipeOutput(output, recipe);
        }
        return null;
    }

    @Nullable
    public final ItemStack getAndConsume(@Nonnull ItemStack[] input) {
        AbstractRecipe recipe = this.recipeConstructor.apply(CachedItemStack.ofArray(input));
        ItemStack output = this.map.get(recipe);
        if (output != null) {
            recipe.consumeRecipe();
            return output;
        }
        return null;
    }

}
