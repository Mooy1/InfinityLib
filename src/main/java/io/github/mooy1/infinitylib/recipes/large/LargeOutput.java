package io.github.mooy1.infinitylib.recipes.large;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class LargeOutput {

    private final ItemStack output;
    private final int[] inputConsumption;
    
}
