package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

@ParametersAreNonnullByDefault
public final class MenuBlockPreset extends BlockMenuPreset {

    private final MenuBlock menuBlock;

    MenuBlockPreset(MenuBlock menuBlock) {
        super(menuBlock.getId(), menuBlock.getItemName());
        this.menuBlock = menuBlock;
    }

    @Override
    public void newInstance(BlockMenu menu, Block b) {
        this.menuBlock.onNewInstance(menu, b);
    }

    @Override
    public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        return this.menuBlock.getTransportSlots(menu, flow, item);
    }

    @Override
    public void init() {
        this.menuBlock.setup(this);
    }

    @Override
    public boolean canOpen(Block b, Player p) {
        return Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK)
                && this.menuBlock.canUse(p, false);
    }

    @Override
    public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
        return new int[0];
    }

    public void addBackground(int slot, @Nullable ItemStack item) {
        addItem(slot, item, ChestMenuUtils.getEmptyClickHandler());
    }

}
