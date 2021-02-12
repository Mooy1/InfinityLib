package io.github.mooy1.infinitylib.recipes.big;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class OutputAndAmounts {

    private final ItemStack output;
    private final int[] amounts;
    
}
