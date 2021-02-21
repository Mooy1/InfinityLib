package io.github.mooy1.infinitylib.recipes.normalstrict;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * An output with the amount to consume from the input
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class StrictOutput {

    private final ItemStack output;
    private final int inputConsumption;
    
}
