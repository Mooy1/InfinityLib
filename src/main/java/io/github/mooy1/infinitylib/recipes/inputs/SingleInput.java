package io.github.mooy1.infinitylib.recipes.inputs;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.StackUtils;

/**
 * A simple recipe which only checks id/material
 */
public class SingleInput {

    private final String id;

    public SingleInput(@Nonnull ItemStack item) {
        this.id = StackUtils.getIDorType(item);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SingleInput && ((SingleInput) obj).id.equals(this.id);
    }

}
