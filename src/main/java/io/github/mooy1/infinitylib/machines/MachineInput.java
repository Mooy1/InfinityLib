package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

final class MachineInput {

    final List<ItemStack> items = new ArrayList<>(2);
    int amount;

    MachineInput(ItemStack item) {
        add(item);
    }

    MachineInput add(ItemStack item) {
        items.add(item);
        amount += item.getAmount();
        return this;
    }

}
