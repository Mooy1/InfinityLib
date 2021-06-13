package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.items.StackUtils;

public final class ShapedRecipe extends AbstractRecipe {

    public ShapedRecipe(ItemStack[] input) {
        super(input);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (ItemStack item : getRawInput()) {
            if (item != null) {
                hash += StackUtils.getIDorType(item).hashCode();
            } else {
                hash += 1;
            }
        }
        return hash;
    }

    @Override
    protected boolean matches(@Nonnull AbstractRecipe input) {
        ItemStack[] inputArr = input.getRawInput();
        ItemStack[] recipeArr = getRawInput();
        for (int i = 0 ; i < inputArr.length ; i++) {
            ItemStack in = inputArr[i];
            ItemStack re = recipeArr[i];
            if (in == null) {
                if (re != null) {
                    return false;
                }
            } else if (re != null && (in.getAmount() < re.getAmount()
                    || !StackUtils.getIDorType(in).equals(StackUtils.getIDorType(re)))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void consume(@Nonnull AbstractRecipe input) {
        ItemStack[] inputArr = input.getRawInput();
        ItemStack[] recipeArr = getRawInput();
        for (int i = 0 ; i < inputArr.length ; i++) {
            ItemStack in = inputArr[i];
            if (in != null) {
                in.setAmount(in.getAmount() - recipeArr[i].getAmount());
            }
        }
    }

}
