package io.github.mooy1.infinitylib.slimefun;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;

/**
 * A slimefun item with a menu and ticker
 */
public abstract class TickingContainer extends SlimefunItem {

    public TickingContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                setupMenu(this);
            }
            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK);
            }
            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                onNewInstance(menu, b);
            }
            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                return getTransportSlots(menu, flow, item);
            }
        };

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return synchronised();
            }
            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);
                if (menu != null) {
                    TickingContainer.this.tick(menu, b, data);
                }
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                BlockMenu menu = BlockStorage.getInventory(e.getBlock());
                if (menu != null) {
                    onBreak(e, menu, e.getBlock().getLocation());
                }
            }
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                onPlace(e, e.getBlockPlaced());
            }
        });

    }

    protected abstract void tick(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config data);

    protected boolean synchronised() {
        return false;
    }
    protected abstract void setupMenu(@Nonnull BlockMenuPreset preset);

    @Nonnull
    protected abstract int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull  ItemTransportFlow flow, ItemStack item);

    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {

    }

    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu, @Nonnull Location l) {

    }

    protected void onPlace(@Nonnull BlockPlaceEvent e, @Nonnull Block b) {

    }

}