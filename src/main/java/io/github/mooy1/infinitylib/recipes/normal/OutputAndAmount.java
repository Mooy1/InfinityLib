package io.github.mooy1.infinitylib.recipes.normal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class OutputAndAmount {

    private final ItemStack output;
    private final int amount;
    
}
