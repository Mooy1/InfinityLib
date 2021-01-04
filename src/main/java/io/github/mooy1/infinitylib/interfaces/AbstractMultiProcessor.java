package io.github.mooy1.infinitylib.interfaces;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AbstractMultiProcessor extends AbstractProcessor {

    Map<MultiFilter, Pair<ItemStack, int[]>> recipes = new HashMap<>();
    Map<ItemFilter, Set<Integer>> slots = new HashMap<>();
    List<ItemStack> displayRecipes = new ArrayList<>();
    int[] inputSlots = {
        10, 11, 12
    };

    @Nonnull
    @Override
    default List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

    int EMPTY = new MultiFilter(FilterType.IGNORE_AMOUNT, new ItemStack[3]).hashCode();

    @Override
    default boolean tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress, int ticks) {
        MultiFilter input = MultiFilter.fromMenu(FilterType.MIN_AMOUNT, menu, inputSlots);
        if (input.hashCode() != EMPTY) {
            Pair<ItemStack, int[]> output = this.recipes.get(input);
            if (output != null) {
                for (int i = 0 ; i < output.getSecondValue().length ; i ++) {
                    int amount = output.getSecondValue()[i];
                    if (amount > 0) {
                        menu.consumeItem(inputSlots[i], amount);
                    }
                }
                progress.getFirstValue().setValue(1);
                progress.setSecondValue(output.getFirstValue().clone());
                setStatus(menu, 1, ticks);
                return true;
            }
        }
        setStatus(menu, 0, ticks);
        return false;
    }
    
    @Override
    default void onBreak(BlockMenu menu, Location l) {
        AbstractProcessor.super.onBreak(menu, l);
        menu.dropItems(l, inputSlots);
        menu.dropItems(l, outputSlot);
    }

    int[] extra = {
            5, 23
    };
    
    int[] inputBorder = {
            0, 1, 2, 3, 4,
            9, 13,
            18, 19, 20, 21, 22
    };
    
    @Override
    default void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        AbstractProcessor.super.setupInv(blockMenuPreset);
        for (int i : MenuPreset.slotChunk3) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : extra) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    default void addRecipe(@Nonnull ItemStack[] input, @Nonnull ItemStack output) {
        ItemStack[] recipe = new ItemStack[3];
        System.arraycopy(input, 0, recipe, 0, 3);
        MultiFilter filter = new MultiFilter(FilterType.MIN_AMOUNT, recipe);
        
        // recipe
        recipes.put(filter, new Pair<>(output, filter.getAmounts()));
        
        // display recipes
        this.displayRecipes.add(recipe[0]);
        this.displayRecipes.add(null);
        this.displayRecipes.add(recipe[1]);
        this.displayRecipes.add(output);
        this.displayRecipes.add(recipe[2]);
        this.displayRecipes.add(null);

        // slots
        for (int i = 0 ; i < recipe.length ; i++) {
            this.slots.computeIfAbsent(new ItemFilter(recipe[i], FilterType.IGNORE_AMOUNT), k -> new HashSet<>(2)).add(inputSlots[i]);
        }
    }
    
    default int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {outputSlot};
        if (flow == ItemTransportFlow.INSERT) {
            ItemFilter filter = new ItemFilter(item, FilterType.IGNORE_AMOUNT);
            for (int i : inputSlots) {
                if (filter.fits(new ItemFilter(menu.getItemInSlot(i), FilterType.IGNORE_AMOUNT))) {
                    return new int[] {i};
                }
            }
            Set<Integer> slots = this.slots.get(filter);
            if (slots != null) {
                for (int i : slots) {
                    if (menu.getItemInSlot(i) != null) return new int[] {i};
                }
            }
        }
        return new int[0];
    }

}
