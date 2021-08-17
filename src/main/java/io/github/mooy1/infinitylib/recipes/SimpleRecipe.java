package io.github.mooy1.infinitylib.recipes;

import org.bukkit.inventory.ItemStack;

final class SimpleRecipe extends AbstractRecipe<ItemStack, SimpleRecipe> {

    private final String string;
    private final int amount;

    SimpleRecipe(ItemStack item) {
        super(item);
        this.string = getString(item);
        this.amount = this.string == AIR ? 0 : item.getAmount();
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleRecipe) {
            SimpleRecipe recipe = ((SimpleRecipe) obj);
            if (this.amount >= recipe.amount && recipe.string.equals(this.string)) {
                this.recipe = recipe;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void consumeRecipe() {
        this.original.setAmount(this.amount - this.recipe.amount);
    }

}
