package io.github.mooy1.infinitylib.slimefun.recipes.outputs;

import io.github.mooy1.infinitylib.slimefun.recipes.RecipeOutput;
import io.github.mooy1.infinitylib.slimefun.recipes.inputs.StrictMultiInput;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class StrictMultiOutput extends RecipeOutput<StrictMultiInput> {

    private final ItemStack output;
    private int[] inputConsumption;

    @Override
    protected void accept(StrictMultiInput input) {
        this.inputConsumption = input.getAmounts();
    }

}
