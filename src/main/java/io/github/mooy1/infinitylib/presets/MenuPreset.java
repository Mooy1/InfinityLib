package io.github.mooy1.infinitylib.presets;

import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Collection of utils for creating BlockMenuPresets
 *
 * @author Mooy1
 * 
 */
public final class MenuPreset {
    
    public static final int[] slotChunk1 = {0, 1, 2, 9, 11, 18, 19, 20};
    public static final int slot1 = 10;

    public static final int[] slotChunk2 = {3, 4, 5, 12, 14, 21, 22, 23};
    public static final int slot2 = 13;

    public static final int[] slotChunk3 = {6, 7, 8, 15, 17, 24, 25, 26};
    public static final int slot3 = 16;
    
    public static void setupBasicMenu(BlockMenuPreset preset) {
        for (int i : slotChunk1) {
            preset.addItem(i, borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : slotChunk2) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : slotChunk3) {
            preset.addItem(i, borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    public static final ItemStack emptyKey = StackUtils.makUnique(new CustomItem(
            Material.BARRIER,
            "&cNo Target",
            "&7Place a item here to set it as the target"
    ));
    public static final ItemStack loadingItemRed = StackUtils.makUnique(new CustomItem(
            Material.RED_STAINED_GLASS_PANE,
            "&cLoading...")
    );
    public static final ItemStack invalidInput = StackUtils.makUnique(new CustomItem(
            Material.BARRIER,
            "&cInvalid Input!")
    );
    public static final ItemStack invisibleBackground = StackUtils.makUnique(new CustomItem(
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            " ")
    );
    public static final ItemStack loadingItemBarrier = StackUtils.makUnique(new CustomItem(
            Material.BARRIER,
            "&cLoading...")
    );
    public static final ItemStack inputAnItem = StackUtils.makUnique(new CustomItem(
            Material.BLUE_STAINED_GLASS_PANE,
            "&9Input an item")
    );
    public static final ItemStack invalidRecipe = StackUtils.makUnique(new CustomItem(
            Material.BARRIER,
            "&cInvalid Recipe!")
    );
    public static final ItemStack notEnoughEnergy = StackUtils.makUnique(new CustomItem(
            Material.RED_STAINED_GLASS_PANE,
            "&cNot enough energy!")
    );
    public static final ItemStack notEnoughRoom = StackUtils.makUnique(new CustomItem(
            Material.ORANGE_STAINED_GLASS_PANE,
            "&6Not enough room!")
    );
    public static final ItemStack borderItemInput = StackUtils.makUnique(new CustomItem(
            Material.BLUE_STAINED_GLASS_PANE,
            "&9Input")
    );
    public static final ItemStack borderItemOutput = StackUtils.makUnique(new CustomItem(
            Material.ORANGE_STAINED_GLASS_PANE,
            "&6Output")
    );
    public static final ItemStack borderItemStatus = StackUtils.makUnique(new CustomItem(
            Material.CYAN_STAINED_GLASS_PANE,
            "&3Status")
    );
    public static final ItemStack connectToEnergyNet = StackUtils.makUnique(new CustomItem(
            Material.RED_STAINED_GLASS_PANE,
            "&cConnect to an energy network!")
    );
}
