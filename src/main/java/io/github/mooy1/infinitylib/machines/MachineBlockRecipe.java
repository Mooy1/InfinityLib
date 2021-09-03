package io.github.mooy1.infinitylib.machines;

import java.util.Map;

import javax.annotation.Nullable;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

@RequiredArgsConstructor
final class MachineBlockRecipe {

    private String[] strings = ArrayUtils.EMPTY_STRING_ARRAY;
    private int[] amounts = ArrayUtils.EMPTY_INT_ARRAY;
    final ItemStack output;

    void add(@Nullable ItemStack item) {
        if (item != null && !item.getType().isAir()) {
            String string = Slimefun.getItemDataService().getItemData(item).orElseGet(() -> item.getType().name());

            int len = strings.length;
            for (int i = 0; i < len; i++) {
                if (strings[i].equals(string)) {
                    amounts[i] += item.getAmount();
                    return;
                }
            }

            String[] expanded = new String[len + 1];
            int[] expandedAmounts = new int[len + 1];

            System.arraycopy(strings, 0, expanded, 0, len);
            System.arraycopy(amounts, 0, expandedAmounts, 0, len);

            expanded[len] = string;
            expandedAmounts[len] = item.getAmount();

            strings = expanded;
            amounts = expandedAmounts;
        }
    }

    boolean check(Map<String, MachineInput> map) {
        for (int i = 0; i < strings.length; i++) {
            MachineInput input = map.get(strings[i]);
            if (input == null || input.amount < amounts[i]) {
                return false;
            }
        }
        return true;
    }

    void consume(Map<String, MachineInput> map) {
        for (int i = 0; i < strings.length; i++) {
            int consume = amounts[i];
            for (ItemStack item : map.get(strings[i]).items) {
                int amt = item.getAmount();
                if (amt >= consume) {
                    item.setAmount(amt - consume);
                    break;
                }
                else {
                    item.setAmount(0);
                    consume -= amt;
                }
            }
        }
    }

}
