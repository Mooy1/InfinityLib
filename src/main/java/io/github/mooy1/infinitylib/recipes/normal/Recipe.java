package io.github.mooy1.infinitylib.recipes.normal;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

final class Recipe {

    private final String id;

    Recipe(@Nonnull ItemStack item) {
        this.id = StackUtils.getIDorType(item);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Recipe && ((Recipe) obj).id.equals(this.id);
    }

}
