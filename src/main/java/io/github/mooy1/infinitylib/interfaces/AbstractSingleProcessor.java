package io.github.mooy1.infinitylib.interfaces;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractSingleProcessor extends AbstractProcessor {

    Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();
    List<ItemStack> displayRecipes = new ArrayList<>();
    int inputSlot = MenuPreset.slot1;

    @Override
    default void addRecipe(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        ItemStack item = input[0];
        recipes.put(new ItemFilter(item, FilterType.MIN_AMOUNT), new Pair<>(output, item.getAmount()));
        displayRecipes.add(item);
        displayRecipes.add(output);
    }

    @Override
    default boolean tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress, int ticks) {
        ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> output = this.recipes.get(new ItemFilter(input, FilterType.MIN_AMOUNT));
            if (output != null) {
                menu.consumeItem(inputSlot, output.getSecondValue());
                progress.getFirstValue().setValue(1);
                progress.setSecondValue(output.getFirstValue().clone());
                setStatus(menu, 1, ticks);
                return true;
            }
        }
        setStatus(menu, 0, ticks);
        return false;
    }

    @Nonnull
    @Override
    default List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

    @Override
    default void onBreak(BlockMenu menu, Location l) {
        AbstractProcessor.super.onBreak(menu, l);
        menu.dropItems(l, inputSlot, outputSlot);
    }
    
    @Override
    default void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        AbstractProcessor.super.setupInv(blockMenuPreset);
        MenuPreset.setupBasicMenu(blockMenuPreset);
    }

    @Override
    default int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {outputSlot};
        if (flow == ItemTransportFlow.INSERT) return new int[] {inputSlot};
        return new int[0];
    }


}
