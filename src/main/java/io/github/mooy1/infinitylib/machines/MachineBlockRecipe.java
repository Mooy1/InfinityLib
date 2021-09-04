package io.github.mooy1.infinitylib.machines;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.common.StackUtils;

final class MachineBlockRecipe {

    private final String[] strings;
    private final int[] amounts;
    final ItemStack output;

    MachineBlockRecipe(ItemStack output, ItemStack[] input) {
        this.output = output;

        Map<String, Integer> strings = new HashMap<>();
        for (ItemStack item : input) {
            if (item != null && !item.getType().isAir()) {
                String string = StackUtils.getId(item);
                if (string == null) {
                    string = item.getType().name();
                }
                strings.compute(string, (k, v) -> v == null ? item.getAmount() : v + item.getAmount());
            }
        }

        this.strings = strings.keySet().toArray(new String[0]);
        this.amounts = ArrayUtils.toPrimitive(strings.values().toArray(new Integer[0]));
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
