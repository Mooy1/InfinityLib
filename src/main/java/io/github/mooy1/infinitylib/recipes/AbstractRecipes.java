package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
abstract class AbstractRecipes<I, R extends AbstractRecipe<I, R>> {

    private final Map<R, ItemStack> map = new HashMap<>();
    private final Function<I, R> constructor;
    private R lastInput;

    public final void addRecipe(@Nullable I input, @Nullable ItemStack output) {
        this.map.compute(this.constructor.apply(input), (recipe, out) -> {
            if (out != null) {
                throw new IllegalArgumentException("The given input is already mapped to an output!");
            }
            return output;
        });
    }

    @Nullable
    public final ItemStack getOutput(@Nullable I input) {
        R in = this.constructor.apply(input);
        ItemStack output = this.map.get(in);
        if (output != null) {
            this.lastInput = in;
        } else {
            this.lastInput = null;
        }
        return output;
    }

    public final void consumeLastRecipe() {
        Objects.requireNonNull(this.lastInput, "No last recipe exists!");
        this.lastInput.consumeRecipe();
        this.lastInput = null;
    }

    public final void copyRecipes(@Nonnull AbstractRecipes<I, R> recipes) {
        this.map.putAll(recipes.map);
    }

}
