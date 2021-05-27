package io.github.mooy1.infinitylib.recipes;

import javax.annotation.Nonnull;

import io.github.mooy1.infinitylib.items.CachedItemStack;

public final class ShapedRecipe extends AbstractRecipe {

    public ShapedRecipe(CachedItemStack[] input) {
        super(input);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (CachedItemStack item : getInput()) {
            if (item != null) {
                hash += item.getIDorType().hashCode();
            } else {
                hash += 1;
            }
        }
        return hash;
    }

    @Override
    protected boolean equals(@Nonnull AbstractRecipe recipe) {
        CachedItemStack[] inputArr = getInput();
        CachedItemStack[] recipeArr = recipe.getInput();
        for (int i = 0 ; i < inputArr.length ; i++) {
            CachedItemStack in = inputArr[i];
            CachedItemStack re = recipeArr[i];
            if (in == null) {
                if (re != null) {
                    return false;
                }
            } else if (re != null && (in.getAmount() < re.getAmount() || !in.softEquals(re))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void consume(AbstractRecipe recipe) {
        CachedItemStack[] inputArr = getInput();
        CachedItemStack[] recipeArr = recipe.getInput();
        for (int i = 0 ; i < inputArr.length ; i++) {
            CachedItemStack in = inputArr[i];
            if (in != null) {
                in.setAmount(in.getAmount() - recipeArr[i].getAmount());
            }
        }
    }

}
