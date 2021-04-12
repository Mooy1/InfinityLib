package io.github.mooy1.infinitylib.slimefun.abstracts;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A slimefun item with a ticker
 */
public abstract class AbstractTicker extends SlimefunItem {

    public AbstractTicker(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return synchronised();
            }
            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                    AbstractTicker.this.tick(b, data);
            }
        });
        
    }

    protected abstract void tick(@Nonnull Block b, @Nonnull Config data);
    
    protected boolean synchronised() {
        return false;
    }
    
}
