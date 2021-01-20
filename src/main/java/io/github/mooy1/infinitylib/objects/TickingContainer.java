package io.github.mooy1.infinitylib.objects;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * slimefun item implementing must call register on itself
 * adds a ticker, menu, place handler, and break handler
 */
public interface TickingContainer {
    
    void tick(@Nonnull BlockMenu menu, @Nonnull Block b);

    void setupMenu(@Nonnull BlockMenuPreset preset);

    @Nonnull
    int[] getTransportSlots(@Nonnull ItemTransportFlow flow);

    @Nonnull
    default int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull  ItemTransportFlow flow, ItemStack item) {
        return getTransportSlots(flow);
    }

    default boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
        return true;
    }

    default void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {

    }

    default void onBreak(@Nonnull BlockMenu menu, @Nonnull BlockBreakEvent e) {

    }

    default void onPlace(@Nonnull BlockPlaceEvent e) {

    }

    /**
     * Use this to register your UpgradeableBlock
     */
    @OverridingMethodsMustInvokeSuper
    default void register(@Nonnull SlimefunItem item) {
        item.addItemHandler((BlockBreakHandler) (e, item1, fortune, drops) -> {
            BlockMenu menu = BlockStorage.getInventory(e.getBlock());
            if (menu != null) {
                onBreak(menu, e);
            }
            return true;
        });
        item.addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                onPlace(e);
            }
        });
        item.addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);
                if (menu != null) {
                    TickingContainer.this.tick(menu, b);
                }
            }
        });
        new BlockMenuPreset(item.getId(), item.getItemName()) {
            @Override
            public void init() {
                setupMenu(this);
            }
            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || (canOpen(b, p) || SlimefunPlugin.getProtectionManager().hasPermission(p, b, ProtectableAction.INTERACT_BLOCK));
            }
            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                TickingContainer.this.onNewInstance(menu, b);
            }
            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                return TickingContainer.this.getTransportSlots(menu, flow, item);
            }
        };
    }

}
