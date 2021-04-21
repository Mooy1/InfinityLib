package io.github.mooy1.infinitylib.items;

import javax.annotation.Nonnull;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.events.AutoDisenchantEvent;

public final class ShinyEnchant extends Enchantment implements Listener {
    
    public ShinyEnchant(@Nonnull AbstractAddon addon, @Nonnull String key) {
        super(addon.getKey(key));
        addon.registerListener(this);
        registerEnchantment(this);
    }
    
    @EventHandler
    public void onDisenchant(@Nonnull AutoDisenchantEvent e) {
        if (e.getItem().getEnchantmentLevel(this) != 0) {
            e.setCancelled(true);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Shiny";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@Nonnull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@Nonnull ItemStack item) {
        return true;
    }

}
