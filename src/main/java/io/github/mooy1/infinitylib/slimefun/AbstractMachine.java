package io.github.mooy1.infinitylib.slimefun;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.cscorelib2.blocks.BlockPosition;

public abstract class AbstractMachine extends AbstractContainer implements EnergyNetComponent {

    private final Map<BlockPosition, Object> processing = new ConcurrentHashMap<>();

    public AbstractMachine(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected final void tick(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config data) {

    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void setupMenu(@Nonnull BlockMenuPreset preset) {

    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    protected abstract int getConsumption();

    protected abstract int getStatusSlot();

}
