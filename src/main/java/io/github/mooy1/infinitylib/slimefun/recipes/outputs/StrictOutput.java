package io.github.mooy1.infinitylib.slimefun.recipes.outputs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class StrictOutput {

    private final ItemStack output;
    @Setter
    private int inputConsumption;
    
}
