package io.github.mooy1.infinitylib.recipes.small;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import io.github.mooy1.infinitylib.items.FastItemStack;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class SmallRecipe {

    @Getter
    private final FastItemStack input;
    private SmallRecipe matchingRecipe;

    @Override
    public int hashCode() {
        if (this.input != null) {
            return this.input.getIDorType().hashCode();
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SmallRecipe) {
            FastItemStack recipe = (this.matchingRecipe = (SmallRecipe) obj).input;
            return this.input.getAmount() >= recipe.getAmount() && this.input.fastEquals(recipe);
        }
        return false;
    }

    public void consume() {
        this.input.setAmount(this.input.getAmount() - getMatchingRecipe().input.getAmount());
    }

    public SmallRecipe getMatchingRecipe() {
        return this.matchingRecipe;
    }

}
