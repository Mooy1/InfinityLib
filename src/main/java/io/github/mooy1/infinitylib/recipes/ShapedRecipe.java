package io.github.mooy1.infinitylib.recipes;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;

final class ShapedRecipe extends AbstractRecipe<ItemStack[], ShapedRecipe> {

    @Getter
    private final int hashCode;
    private final String[] strings;
    private final int[] amounts;

    public ShapedRecipe(ItemStack[] input) {
        super(input);
        int hash = 0;
        this.strings = new String[input.length];
        this.amounts = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            String string = this.strings[i] = getString(input[i]);
            this.amounts[i] = string == AIR ? 0 : input[i].getAmount();
            hash += string.hashCode();
        }
        this.hashCode = hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShapedRecipe) {
            ShapedRecipe recipe = ((ShapedRecipe) obj);
            for (int i = 0; i < this.original.length; i++) {
                if (this.amounts[i] < recipe.amounts[i] || !this.strings[i].equals(recipe.strings[i])) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override
    protected void consumeRecipe() {
        for (int i = 0; i < this.original.length; i++) {
            if (this.strings[i] != AIR) {
                this.original[i].setAmount(this.amounts[i] - this.recipe.amounts[i]);
            }
        }
    }

}
