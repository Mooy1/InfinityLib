package io.github.mooy1.infinitylib.interfaces;

import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public interface EnergyContainer extends TickingContainer, EnergyNetComponent {

    int getEnergyConsumption();
    
    int getStatusSlot();
    
    boolean process(@Nonnull BlockMenu menu, @Nonnull Block b);

    @Override
    default void tick(@Nonnull BlockMenu menu, @Nonnull Block b) {
        int energy = getEnergyConsumption();
        if (getCharge(b.getLocation()) < energy) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(getStatusSlot(), MenuPreset.notEnoughEnergy);
            }
        } else if (process(menu, b)) {
            removeCharge(b.getLocation(), energy);
        }
    }
    
    @Nonnull
    @Override
    default EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    default void setupMenu(@Nonnull BlockMenuPreset preset) {
        preset.addItem(getStatusSlot(), MenuPreset.loadingItemRed, ChestMenuUtils.getEmptyClickHandler());
    }
    
}
