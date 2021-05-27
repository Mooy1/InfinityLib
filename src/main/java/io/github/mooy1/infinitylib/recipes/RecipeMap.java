package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RecipeMap<T> {

    private final Map<RecipeInput, ItemStack> map = new HashMap<>();

    private RecipeInput lastInput;

    public void put(@Nonnull ItemStack[] recipe, @Nonnull ItemStack output) {
        this.map.put(create(recipe), output);
    }

    @Nullable
    public final ItemStack get(@Nonnull ItemStack[] input) {
        return this.map.get(this.lastInput = create(input));
    }

    public final void consumeLast() {
        consume(this.lastInput, this.lastInput.lastRecipe);
    }

    @Nullable
    public final ItemStack getAndConsume(@Nonnull ItemStack[] input) {
        ItemStack output = get(input);
        if (output != null) {
            consumeLast();
        }
        return output;
    }

    @Nonnull
    protected abstract RecipeInput create(@Nonnull ItemStack[] inputs);

    protected abstract void consume(@Nonnull RecipeInput input, @Nonnull RecipeInput recipe);

    protected abstract boolean equals(@Nonnull T input, @Nonnull T recipe);

    protected abstract int hashCode(@Nonnull ItemStack[] input, @Nonnull T data);

    @RequiredArgsConstructor
    protected final class RecipeInput {

        @Getter
        private final ItemStack[] input;

        @Getter
        private final T data;

        private RecipeInput lastRecipe;

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (!(obj instanceof RecipeMap.RecipeInput)) {
                return false;
            }
            return RecipeMap.this.equals(this.data, (this.lastRecipe = (RecipeInput) obj).data);
        }

        @Override
        public int hashCode() {
            return RecipeMap.this.hashCode(this.input, this.data);
        }

    }

}
