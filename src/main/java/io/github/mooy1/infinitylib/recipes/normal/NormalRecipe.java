package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A simple recipe which only checks id/material
 */
final class NormalRecipe {

    private final String id;

    NormalRecipe(@Nonnull ItemStack item) {
        this.id = StackUtils.getIDorType(item);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NormalRecipe && ((NormalRecipe) obj).id.equals(this.id);
    }

}
