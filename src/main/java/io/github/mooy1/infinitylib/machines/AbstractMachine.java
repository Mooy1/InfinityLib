package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;

public abstract class AbstractMachine extends TickingMenuBlock implements EnergyNetComponent {

    @Override
    protected void setup(MenuBlockPreset preset) {
        preset.drawBackground(OUTPUT_BORDER, this.outputBorder);
        preset.drawBackground(INPUT_BORDER, this.inputBorder);
        preset.drawBackground(BACKGROUND_ITEM, this.background);
        preset.addItem(this.statusSlot, IDLE_ITEM, EMPTY_CLICK_HANDLER);
    }

    @Override
    protected final int[] getInputSlots() {
        return this.inputs;
    }

    @Override
    protected final int[] getOutputSlots() {
        return this.outputs;
    }

    @Override
    public final int getCapacity() {
        return this.energyCapacity;
    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

}
