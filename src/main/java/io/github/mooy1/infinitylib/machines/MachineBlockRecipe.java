package io.github.mooy1.infinitylib.machines;

import java.util.Optional;

import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

@RequiredArgsConstructor
final class MachineBlockRecipe {

    final ItemStack output;
    String[] strings = ArrayUtils.EMPTY_STRING_ARRAY;
    int[] amounts = ArrayUtils.EMPTY_INT_ARRAY;

    void add(@Nullable ItemStack item) {
        if (item != null && !item.getType().isAir()) {
            String string = Slimefun.getItemDataService().getItemData(item).orElseGet(() -> item.getType().name());

            int len = this.strings.length;
            for (int i = 0; i < len; i++) {
                if (this.strings[i].equals(string)) {
                    this.amounts[i]+= item.getAmount();
                    return;
                }
            }

            String[] expanded = new String[len + 1];
            int[] expandedAmounts = new int[len + 1];

            System.arraycopy(this.strings, 0, expanded, 0, len);
            System.arraycopy(this.amounts, 0, expandedAmounts, 0, len);

            expanded[len] = string;
            expandedAmounts[len] = item.getAmount();

            this.strings = expanded;
            this.amounts = expandedAmounts;
        }
    }

    boolean isValid() {
        return this.strings != ArrayUtils.EMPTY_STRING_ARRAY;
    }

}
