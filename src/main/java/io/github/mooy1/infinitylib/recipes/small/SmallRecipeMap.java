package io.github.mooy1.infinitylib.recipes.small;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.FastItemStack;

public final class SmallRecipeMap<O> {

    private final Map<SmallRecipe, O> recipes = new ConcurrentHashMap<>();

    public void put(@Nonnull ItemStack rawInput, @Nonnull O output) {
        this.recipes.put(toRecipe(rawInput), output);
    }

    @Nullable
    public SmallOutput<O> get(@Nonnull ItemStack rawInput) {
        SmallRecipe input = toRecipe(rawInput);
        O output = this.recipes.get(input);
        if (output != null) {
            return new SmallOutput<>(output, input);
        }
        return null;
    }

    @Nullable
    public O getAndConsume(@Nonnull ItemStack rawInput) {
        SmallRecipe input = toRecipe(rawInput);
        O output = this.recipes.get(input);
        if (output != null) {
            input.consume();
            return output;
        }
        return null;
    }

    @Nullable
    public O getNoConsume(@Nonnull ItemStack rawInput) {
        return this.recipes.get(toRecipe(rawInput));
    }

    @Nonnull
    private SmallRecipe toRecipe(@Nonnull ItemStack rawInput) {
        return new SmallRecipe(FastItemStack.of(rawInput));
    }

}
