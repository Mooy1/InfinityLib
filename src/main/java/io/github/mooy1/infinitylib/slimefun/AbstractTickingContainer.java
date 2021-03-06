package io.github.mooy1.infinitylib.slimefun;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * A slimefun item with a menu and ticker
 */
public abstract class AbstractTickingContainer extends AbstractContainer {

    public AbstractTickingContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return synchronised();
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);
                if (menu != null) {
                    AbstractTickingContainer.this.tick(menu, b);
                }
            }

        });
    }

    protected abstract void tick(@Nonnull BlockMenu menu, @Nonnull Block b);

    protected boolean synchronised() {
        return false;
    }

}