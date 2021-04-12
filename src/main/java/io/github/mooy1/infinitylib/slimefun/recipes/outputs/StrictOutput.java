package io.github.mooy1.infinitylib.slimefun.recipes.outputs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.slimefun.recipes.RecipeOutput;
import io.github.mooy1.infinitylib.slimefun.recipes.inputs.StrictInput;

@Getter
@RequiredArgsConstructor
public class StrictOutput extends RecipeOutput<StrictInput> {

    private final ItemStack output;
    private int inputConsumption;

    @Override
    protected void accept(StrictInput input) {
        this.inputConsumption = input.getAmount();
    }

}
