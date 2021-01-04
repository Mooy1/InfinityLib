package io.github.mooy1.infinitylib.interfaces;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AbstractConverter extends RecipeDisplayItem {
    
    int inputSlot = MenuPreset.slot1;
    int outputSlot = MenuPreset.slot3;

    List<ItemStack> displayRecipes = new ArrayList<>();
    Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();

    @ParametersAreNonnullByDefault
    @OverridingMethodsMustInvokeSuper
    default void onBreak(Location l, BlockMenu menu) {
        menu.dropItems(l, inputSlot, outputSlot);
    }

    default boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        @Nullable ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> pair = this.recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
            if (pair != null) {
                int amount = Math.min(input.getAmount() / pair.getSecondValue(), getMaxInput(b));
                ItemStack output = pair.getFirstValue().clone();
                output.setAmount(output.getAmount() * amount);
                if (menu.fits(output, outputSlot)) {
                    menu.pushItem(output, outputSlot);
                    menu.consumeItem(inputSlot, pair.getSecondValue() * amount);
                    return true;
                }
            }
        }
        return false;
    }
    
    int getMaxInput(Block b);

    default int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {outputSlot};
        if (flow == ItemTransportFlow.INSERT) return new int[] {inputSlot};
        return new int[0];
    }

    default void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        MenuPreset.setupBasicMenu(blockMenuPreset);
    }

    @Nonnull
    @Override
    default List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

    default void addRecipes(boolean inverse, ItemStack... items) {
        if (inverse) {
            for (int i = 0 ; i < items.length ; i +=2) {
                displayRecipes.add(items[i + 1]);
                displayRecipes.add(items[i]);
                recipes.put(new ItemFilter(items[i + 1], FilterType.IGNORE_AMOUNT), new Pair<>(items[i], items[i + 1].getAmount()));
            }
        } else {
            for (int i = 0 ; i < items.length ; i +=2) {
                displayRecipes.add(items[i]);
                displayRecipes.add(items[i + 1]);
                recipes.put(new ItemFilter(items[i], FilterType.IGNORE_AMOUNT), new Pair<>(items[i + 1], items[i].getAmount()));
            }
        }
    }
    
}
