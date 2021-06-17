package io.github.mooy1.infinitylib.recipes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class RecipeMap<O> {

    private final Map<AbstractRecipe, O> recipes = new ConcurrentHashMap<>();
    private final RecipeType type;

    public void put(@Nonnull ItemStack[] rawInput, @Nonnull O output) {
        this.recipes.put(this.type.apply(rawInput), output);
    }

    @Nullable
    public RecipeOutput<O> get(@Nonnull ItemStack[] rawInput) {
        AbstractRecipe input = this.type.apply(rawInput);
        O output = this.recipes.get(input);
        if (output != null) {
            return new RecipeOutput<>(output, input);
        }
        return null;
    }

    @Nullable
    public O getAndConsume(@Nonnull ItemStack[] rawInput) {
        AbstractRecipe input = this.type.apply(rawInput);
        O output = this.recipes.get(input);
        if (output != null) {
            input.consumeMatchingRecipe();
            return output;
        }
        return null;
    }

    @Nullable
    public O getNoConsume(@Nonnull ItemStack[] rawInput) {
        return this.recipes.get(this.type.apply(rawInput));
    }

}
