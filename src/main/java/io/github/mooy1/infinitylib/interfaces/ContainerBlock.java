package io.github.mooy1.infinitylib.interfaces;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
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

public interface ContainerBlock {
    
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

    default void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {

    }

    default void onPlace(@Nonnull BlockPlaceEvent e) {

    }

    /**
     * Use this to register handlers + menu to this SlimefunItem
     */
    @OverridingMethodsMustInvokeSuper
    default void register(@Nonnull SlimefunItem item) {
        new BlockMenuPreset(item.getId(), item.getItemName()) {
            @Override
            public void init() {
                setupMenu(this);
            }
            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || (ContainerBlock.this.canOpen(b, p) || SlimefunPlugin.getProtectionManager().hasPermission(p, b, ProtectableAction.INTERACT_BLOCK));
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
    }
    
}
