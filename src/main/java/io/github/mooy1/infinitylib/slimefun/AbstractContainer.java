package io.github.mooy1.infinitylib.slimefun;

import java.util.List;

import javax.annotation.Nonnull;

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
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;

/**
 * A slimefun item with a menu
 *
 * @author Mooy1
 */
public abstract class AbstractContainer extends SlimefunItem {

    public AbstractContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        new BlockMenuPreset(getId(), getItemName()) {
            
            @Override
            public void init() {
                setupMenu(this);
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return AbstractContainer.canOpen(b, p);
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
        
        addItemHandler(new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                onBreak(e);
            }

        });
        
        addItemHandler(new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                onPlace(e);
            }

        });

        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return AbstractContainer.this.isSynchronized();
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                AbstractContainer.this.tick(b);
            }

        });
    }


    @Nonnull
    protected abstract int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull  ItemTransportFlow flow, ItemStack item);

    protected abstract void tick(@Nonnull Block b);

    protected abstract void setupMenu(@Nonnull BlockMenuPreset preset);
    
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {

    }

    protected void onBreak(@Nonnull BlockBreakEvent e) {

    }

    protected void onPlace(@Nonnull BlockPlaceEvent e) {

    }

    protected boolean isSynchronized() {
        return false;
    }

    public static boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
        return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager()
                .hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK);
    }
    
}
