package io.github.mooy1.infinitylib.utils;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * A class with items for menus
 *
 * @author Mooy1
 */
@UtilityClass
public final class MenuItem {

    public static final ItemStack INVALID_INPUT =
            new CustomItemStack(Material.BARRIER, "&cInvalid Input!");

    public static final ItemStack NO_INPUT =
            new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, "&9Input an item");

    public static final ItemStack NO_ENERGY =
            new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy!");

    public static final ItemStack NO_ROOM =
            new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Not enough room!");

    public static final ItemStack INPUT_BORDER =
            new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, "&9Input");

    public static final ItemStack STATUS_BORDER =
            new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, "&3Status");

    public static final ItemStack OUTPUT_BORDER =
            new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Output");

    public static final ItemStack LOADING =
            new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, "&cLoading...");

}
