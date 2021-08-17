package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
abstract class AbstractRecipe<I, R extends AbstractRecipe<I, R>> {

    protected static final String AIR = Material.AIR.name();

    protected final I original;
    protected R recipe;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    protected abstract void consumeRecipe();

    @Nonnull
    protected static String getString(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return AIR;
        } else {
            String id = RecipeHelper.getId(item);
            return id == null ? item.getType().name() : id;
        }
    }

}
