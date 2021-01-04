package io.github.mooy1.infinitylib.interfaces;

import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.Map;

public interface AbstractProcessor extends RecipeDisplayItem {

    Map<Location, Pair<MutableInt, ItemStack>> progressing = new HashMap<>();
    int outputSlot = MenuPreset.slot3;
    int statusSlot = MenuPreset.slot2;
    
    int getTicks(Block b);
    String getProcessing();
    
    @OverridingMethodsMustInvokeSuper
    default void onBreak(BlockMenu menu, Location l) {
        progressing.remove(l);
    }
    
    default boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Pair<MutableInt, ItemStack> progress = progressing.computeIfAbsent(b.getLocation(), k -> new Pair<>(new MutableInt(0), null));
        int ticks = getTicks(b);
        if (progress.getFirstValue().intValue() == 0) {
            return tryStart(menu, progress, ticks);
        } else if (progress.getFirstValue().intValue() >= ticks) {
            if (menu.fits(progress.getSecondValue(), outputSlot)) {
                menu.pushItem(progress.getSecondValue(), outputSlot);
                progress.getFirstValue().setValue(0);
                progress.setSecondValue(null);
                return tryStart(menu, progress, ticks);
            }
        } else {
            progress.getFirstValue().increment();
            setStatus(menu, progress.getFirstValue().intValue(), ticks);
            return true;
        }
        setStatus(menu, 0, ticks);
        return false;
    }

    boolean tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress, int ticks);

    void addRecipe(@Nonnull ItemStack[] input, @Nonnull ItemStack output);
    
    @OverridingMethodsMustInvokeSuper
    default void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.addItem(statusSlot, getStatus(0, 0), ChestMenuUtils.getEmptyClickHandler());
    }

    default void setStatus(BlockMenu menu, int current, int ticks) {
        if (menu.hasViewer())  {
            menu.replaceExistingItem(statusSlot, getStatus(current, ticks), false);
        }
    }

    default ItemStack getStatus(int current, int ticks) {
        if (current == 0) {
            return new CustomItem(Material.BARRIER, "&cIdle...");
        } else {
            return new CustomItem(Material.PISTON, "&a" + this.getProcessing() + "... " + current + "/" + ticks);
        }
    }
    
    int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack item);
    
}
