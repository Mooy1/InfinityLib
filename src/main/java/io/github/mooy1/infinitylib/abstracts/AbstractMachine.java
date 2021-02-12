package io.github.mooy1.infinitylib.abstracts;

import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A slimefun item with a menu and ticker, which will process if it has enough energy
 * 
 * @author Mooy1
 */
public abstract class AbstractMachine extends AbstractTicker implements EnergyNetComponent {

    protected final int statusSlot;
    protected final int energy;
    
    public AbstractMachine(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int statusSlot, int energyConsumption) {
        super(category, item, recipeType, recipe);
        this.statusSlot = statusSlot;
        this.energy = energyConsumption;
    }
    
    protected abstract boolean process(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config data);

    @Override
    protected final void tick(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config data) {
        if (getCharge(b.getLocation()) < this.energy) {
            if (this.statusSlot != -1 && menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MenuPreset.notEnoughEnergy);
            }
        } else if (process(menu, b, data)) {
            removeCharge(b.getLocation(), this.energy);
        }
    }
    
    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }
    
}
