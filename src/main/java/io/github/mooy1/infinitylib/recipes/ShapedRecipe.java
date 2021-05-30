package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import io.github.mooy1.infinitylib.items.FastItemStack;

public final class ShapedRecipe extends AbstractRecipe {

    public ShapedRecipe(FastItemStack[] input) {
        super(input);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (FastItemStack item : getRawInput()) {
            if (item != null) {
                hash += item.getIDorType().hashCode();
            } else {
                hash += 1;
            }
        }
        return hash;
    }

    @Override
    protected boolean matches(@Nonnull AbstractRecipe input) {
        FastItemStack[] inputArr = input.getRawInput();
        FastItemStack[] recipeArr = getRawInput();
        for (int i = 0 ; i < inputArr.length ; i++) {
            FastItemStack in = inputArr[i];
            FastItemStack re = recipeArr[i];
            if (in == null) {
                if (re != null) {
                    return false;
                }
            } else if (re != null && (in.getAmount() < re.getAmount() || !in.fastEquals(re))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void consume(@Nonnull AbstractRecipe input) {
        FastItemStack[] inputArr = input.getRawInput();
        FastItemStack[] recipeArr = getRawInput();
        for (int i = 0 ; i < inputArr.length ; i++) {
            FastItemStack in = inputArr[i];
            if (in != null) {
                in.setAmount(in.getAmount() - recipeArr[i].getAmount());
            }
        }
    }

}
