package io.github.mooy1.infinitylib.recipes;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.github.mooy1.infinitylib.utils.RecipeHelper;

final class SimpleRecipe {

    private final boolean nullOrAir;
    private final ItemStack item;
    private final Material type;
    private final String id;
    @Getter
    private final int hashCode;

    private SimpleRecipe lastMatch;

    SimpleRecipe(ItemStack item) {
        this.item = item;
        if (item == null || item.getType().isAir()) {
            this.nullOrAir = true;
            this.id = null;
            this.hashCode = 0;
        } else {
            this.nullOrAir = false;
            this.id = RecipeHelper.getId(item);
            if (this.id == null) {
                this.type = item.getType();
                this.hashCode = this.type.name().hashCode();
            } else {
                this.type = null;
                this.hashCode = this.id.hashCode();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // TODO check which is recipe
        if (obj instanceof SimpleRecipe) {
            SimpleRecipe other = ((SimpleRecipe) obj);
            if (this.nullOrAir) {
                return other.nullOrAir;
            } else if (other.nullOrAir) {
                return false;
            } else if (this.id == null) {
                if (other.id == null) {
                    return this.type == other.type;
                } else {
                    return false;
                }
            } else if (other.id == null) {
                return false;
            } else {
                return this.id.equals(other.id);
            }
        }
        return false;
    }

}
