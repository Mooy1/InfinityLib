package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
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
        return item == null || item.getType().isAir() ? AIR
                : Slimefun.getItemDataService().getItemData(item).orElseGet(() -> item.getType().name());
    }

}
