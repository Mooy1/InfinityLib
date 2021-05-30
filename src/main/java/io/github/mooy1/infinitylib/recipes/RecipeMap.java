package io.github.mooy1.infinitylib.recipes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.FastItemStack;

@RequiredArgsConstructor
public final class RecipeMap<O> {

    private final Function<FastItemStack[], ? extends AbstractRecipe> recipeConstructor;

    private final Map<AbstractRecipe, O> recipes = new ConcurrentHashMap<>();

    public void put(@Nonnull ItemStack[] rawInput, @Nonnull O output) {
        this.recipes.put(toRecipe(rawInput), output);
    }

    @Nullable
    public RecipeOutput<O> get(@Nonnull ItemStack[] rawInput) {
        AbstractRecipe input = toRecipe(rawInput);
        O output = this.recipes.get(input);
        if (output != null) {
            return new RecipeOutput<>(output, input);
        }
        return null;
    }

    @Nullable
    public O getAndConsume(@Nonnull ItemStack[] rawInput) {
        AbstractRecipe input = toRecipe(rawInput);
        O output = this.recipes.get(input);
        if (output != null) {
            input.consumeMatchingRecipe();
            return output;
        }
        return null;
    }

    @Nullable
    public O getNoConsume(@Nonnull ItemStack[] rawInput) {
        return this.recipes.get(toRecipe(rawInput));
    }

    @Nonnull
    private AbstractRecipe toRecipe(@Nonnull ItemStack[] rawInput) {
        return this.recipeConstructor.apply(FastItemStack.fastArray(rawInput));
    }

}
