package io.github.mooy1.infinitylib.machines;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.common.Translations;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

@ParametersAreNonnullByDefault
public abstract class MenuBlock extends SlimefunItem {

    public static final ItemStack PROCESSING_ITEM = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE,
            ChatColor.GREEN + Translations.get("machines.processing-item", "Processing..."));
    public static final ItemStack NO_ENERGY_ITEM = new CustomItemStack(Material.RED_STAINED_GLASS_PANE,
            ChatColor.RED + Translations.get("machines.no-energy-item", "Not enough energy!"));
    public static final ItemStack IDLE_ITEM = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE,
            ChatColor.BLACK + Translations.get("machines.idle-item", "Idle"));
    public static final ItemStack NO_ROOM_ITEM = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
            ChatColor.GOLD + Translations.get("machines.no-room-item", "Not enough room!"));
    public static final ItemStack OUTPUT_BORDER = new CustomItemStack(ChestMenuUtils.getOutputSlotTexture(),
            ChatColor.GOLD + Translations.get("machines.output-border", "Output"));
    public static final ItemStack INPUT_BORDER = new CustomItemStack(ChestMenuUtils.getInputSlotTexture(),
            ChatColor.BLUE + Translations.get("machines.input-border", "Input"));
    public static final ItemStack BACKGROUND_ITEM = ChestMenuUtils.getBackground();

    public MenuBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack itemStack, List<ItemStack> list) {
                BlockMenu menu = BlockStorage.getInventory(e.getBlock());
                if (menu != null) {
                    onBreak(e, menu);
                }
            }

        }, new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                onPlace(e, e.getBlockPlaced());
            }

        });
    }

    @Override
    public final void postRegister() {
        new MenuBlockPreset(this);
    }

    protected abstract void setup(BlockMenuPreset preset);

    @Nonnull
    protected final int[] getTransportSlots(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        switch (flow) {
            case INSERT:
                return getInputSlots(menu, item);
            case WITHDRAW:
                return getOutputSlots();
            default:
                return new int[0];
        }
    }

    protected int[] getInputSlots(DirtyChestMenu menu, ItemStack item) {
        return getInputSlots();
    }

    protected abstract int[] getInputSlots();

    protected abstract int[] getOutputSlots();

    protected void onNewInstance(BlockMenu menu, Block b) {

    }

    protected void onBreak(BlockBreakEvent e, BlockMenu menu) {
        Location l = menu.getLocation();
        menu.dropItems(l, getInputSlots());
        menu.dropItems(l, getOutputSlots());
    }

    protected void onPlace(BlockPlaceEvent e, Block b) {

    }

}
