package io.github.mooy1.infinitylib.objects;

import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public abstract class AbstractEnergyMachine extends AbstractContainer implements EnergyNetComponent {
    
    private final int statusSlot;
    private final int energy;
    
    public AbstractEnergyMachine(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int statusSlot, int energy) {
        super(category, item, recipeType, recipe);
        this.statusSlot = statusSlot;
        this.energy = energy;
    }

    @Override
    public void tick(@Nonnull Block b, @Nonnull BlockMenu inv) {
        if (getCharge(b.getLocation()) < this.energy) {
            if (inv.hasViewer()) {
                inv.replaceExistingItem(this.statusSlot, MenuPreset.notEnoughEnergy);
            }
        } else if (process(b, inv)) {
            removeCharge(b.getLocation(), this.energy);
        }
    }
    
    public abstract boolean process(@Nonnull Block b, @Nonnull BlockMenu inv);

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

}
