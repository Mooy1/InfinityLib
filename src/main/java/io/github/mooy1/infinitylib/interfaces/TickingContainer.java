package io.github.mooy1.infinitylib.interfaces;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.annotation.Nonnull;

/**
 * slimefun item implementing must call register on itself
 * adds a ticker, menu, place handler, and break handler
 */
public interface TickingContainer extends ContainerBlock {
    
    void tick(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config data);
    
    /**
     * Use this to register the TickingBlock handlers to this SlimefunItem
     */
    @Override
    default void register(@Nonnull SlimefunItem item) {
        ContainerBlock.super.register(item);
        item.addItemHandler((BlockBreakHandler) (e, item1, fortune, drops) -> {
            BlockMenu menu = BlockStorage.getInventory(e.getBlock());
            if (menu != null) {
                onBreak(e, menu);
            }
            return true;
        }, new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                onPlace(e);
            }
        }, new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }
            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);
                if (menu != null) {
                    TickingContainer.this.tick(menu, b, data);
                }
            }
        });
    }

}
