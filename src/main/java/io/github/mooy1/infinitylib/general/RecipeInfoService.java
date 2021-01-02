package io.github.mooy1.infinitylib.general;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class RecipeInfoService {

    public static final ItemStack ITEM = new CustomItem(Material.BOOK, "&6Recipes", "", "&e > Click to see recipes made in this machine");
    
    public static ChestMenu.MenuClickHandler getHandler(boolean generator, List<ItemStack> items) {
        return (player, i, itemStack, clickAction) -> {
            open(player, generator, items);
            return false;
        };
    }
    
    private static void open(Player p, boolean generator, List<ItemStack> items) {
        ChestMenu menu = new ChestMenu("Recipes");
        menu.setEmptySlotsClickable(false);
        menu.setPlayerInventoryClickable(false);
        
        
        
        menu.open(p);
    }
    
}
