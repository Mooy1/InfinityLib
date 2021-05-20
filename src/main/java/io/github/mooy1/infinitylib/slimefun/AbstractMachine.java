package io.github.mooy1.infinitylib.slimefun;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractMachine extends AbstractContainer {

    private int interval = -1;
    private int capacity = -1;
    private int energy = -1;

    public AbstractMachine(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    protected final int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, ItemStack item) {
        return new int[0];
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void setupMenu(@Nonnull BlockMenuPreset preset) {

    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void preRegister() {
        if (this.interval == -1 || this.capacity == -1 || this.energy == -1) {
            throw new IllegalStateException("You must set the interval, capacity, energy, of an AbstractMachine before registering it!");
        }
    }

    public AbstractMachine interval(int interval) {
        this.interval = interval;
        return this;
    }

    public AbstractMachine capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public AbstractMachine energy(int energy) {
        this.energy = energy;
        return this;
    }



}
