package io.github.mooy1.infinitylib.recipes;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.FastItemStack;

@RequiredArgsConstructor
public final class RecipeMap<T extends AbstractRecipe> {

    private final Function<FastItemStack[], T> recipeConstructor;

    private final Set<T> recipes = new HashSet<>();

    public void add(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        T recipe = this.recipeConstructor.apply(FastItemStack.ofArray(input));
        recipe.setOutput(output);
        this.recipes.add(recipe);
    }

    @Nullable
    public final T get(@Nonnull ItemStack input) {
        T recipe = this.recipeConstructor.apply(new FastItemStack[] { FastItemStack.of(input) });
        if (this.recipes.contains(recipe)) {
            return recipe;
        }
        return null;
    }

    @Nullable
    public final T get(@Nonnull ItemStack[] input) {
        T recipe = this.recipeConstructor.apply(FastItemStack.ofArray(input));
        if (this.recipes.contains(recipe)) {
            return recipe;
        }
        return null;
    }

}
