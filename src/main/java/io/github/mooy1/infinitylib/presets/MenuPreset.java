package io.github.mooy1.infinitylib.presets;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

/**
 * Collection of utils for creating BlockMenuPresets
 *
 * @author Mooy1
 * 
 */
@UtilityClass
public final class MenuPreset {
    
    public static final int[] slotChunk1 = {0, 1, 2, 9, 11, 18, 19, 20};
    public static final int slot1 = 10;

    public static final int[] slotChunk2 = {3, 4, 5, 12, 14, 21, 22, 23};
    public static final int slot2 = 13;

    public static final int[] slotChunk3 = {6, 7, 8, 15, 17, 24, 25, 26};
    public static final int slot3 = 16;
    
    public static final int[] craftingInputBorder = {
            0, 1, 2, 3, 4,
            9, 13,
            18, 22,
            27, 31,
            36, 37, 38, 39, 40
    };

    public static final int[] craftingInput = {
            10, 11, 12,
            19, 20, 21,
            28, 29, 30
    };
    public static final int[] craftingBackground = {
            5, 6, 7, 14, 8, 23,
            41, 42, 43, 44, 32
    };

    public static final int[] craftingOutput = {25};
    public static final int[] craftingOutputBorder = {24, 26, 15, 16, 17, 33, 34, 35};
    
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

    private static final NamespacedKey key = PluginUtils.getKey("unique");


    @Nonnull
    public static ItemStack getUnique(@Nonnull ItemStack item) {
        return makeUnique(item.clone());
    }

    @Nonnull
    public static ItemStack makeUnique(@Nonnull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }
    
    public static final ItemStack emptyKey = makeUnique(new CustomItem(
            Material.BARRIER,
            "&cNo Target",
            "&7Place a item here to set it as the target"
    ));
    public static final ItemStack loadingItemRed = makeUnique(new CustomItem(
            Material.RED_STAINED_GLASS_PANE,
            "&cLoading...")
    );
    public static final ItemStack invalidInput = makeUnique(new CustomItem(
            Material.BARRIER,
            "&cInvalid Input!")
    );
    public static final ItemStack invisibleBackground = makeUnique(new CustomItem(
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            " ")
    );
    public static final ItemStack loadingItemBarrier = makeUnique(new CustomItem(
            Material.BARRIER,
            "&cLoading...")
    );
    public static final ItemStack inputAnItem = makeUnique(new CustomItem(
            Material.BLUE_STAINED_GLASS_PANE,
            "&9Input an item")
    );
    public static final ItemStack invalidRecipe = makeUnique(new CustomItem(
            Material.BARRIER,
            "&cInvalid Recipe!")
    );
    public static final ItemStack notEnoughEnergy = makeUnique(new CustomItem(
            Material.RED_STAINED_GLASS_PANE,
            "&cNot enough energy!")
    );
    public static final ItemStack notEnoughRoom = makeUnique(new CustomItem(
            Material.ORANGE_STAINED_GLASS_PANE,
            "&6Not enough room!")
    );
    public static final ItemStack borderItemInput = makeUnique(new CustomItem(
            Material.BLUE_STAINED_GLASS_PANE,
            "&9Input")
    );
    public static final ItemStack borderItemOutput = makeUnique(new CustomItem(
            Material.ORANGE_STAINED_GLASS_PANE,
            "&6Output")
    );
    public static final ItemStack borderItemStatus = makeUnique(new CustomItem(
            Material.CYAN_STAINED_GLASS_PANE,
            "&3Status")
    );
    public static final ItemStack connectToEnergyNet = makeUnique(new CustomItem(
            Material.RED_STAINED_GLASS_PANE,
            "&cConnect to an energy network!")
    );
    
}
