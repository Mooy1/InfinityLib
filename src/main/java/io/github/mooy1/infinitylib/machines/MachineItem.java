package io.github.mooy1.infinitylib.machines;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

/**
 * A class with items for menus
 *
 * @author Mooy1
 */
@UtilityClass
public final class MachineItem {

    public static final ItemStack NO_ENERGY = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy!");
    public static final ItemStack NO_ROOM = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Not enough room!");
    public static final ItemStack INPUT_BORDER = new CustomItemStack(ChestMenuUtils.getInputSlotTexture(), "&9Input");
    public static final ItemStack OUTPUT_BORDER = new CustomItemStack(ChestMenuUtils.getOutputSlotTexture(), "&6Output");
    public static final ItemStack IDLE = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&8Idle");
    public static final ItemStack BACKGROUND = ChestMenuUtils.getBackground();

}
