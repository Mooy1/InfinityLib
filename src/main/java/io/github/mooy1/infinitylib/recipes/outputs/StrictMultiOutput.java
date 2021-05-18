package io.github.mooy1.infinitylib.recipes.outputs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class StrictMultiOutput {

    private final ItemStack output;
    private final int[] inputConsumption;

}
