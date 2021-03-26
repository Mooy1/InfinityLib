package io.github.mooy1.infinitylib.slimefun.recipes.outputs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class StrictMultiOutput {

    private final ItemStack output;
    @Setter
    private int[] inputConsumption;
    
}
