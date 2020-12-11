package io.github.mooy1.infinitylib.items;

import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.NonNull;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
public final class StackUtils {
    
    private static final CustomItemDataService service = SlimefunPlugin.getItemDataService();

    /**
     * Gets the slimefun item id of an item, otherwise if vanilla true returns the material id
     */
    @Nullable
    public static String getItemID(ItemStack item, boolean vanilla) {

        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }

        Optional<String> itemID = service.getItemData(item);

        if (itemID.isPresent()) {
            return itemID.get();
        }

        if (vanilla) {
            return item.getType().toString();
        }

        return null;
    }

    /**
     * Gets the item from an id, calls {@link SlimefunItem}#getByItem
     */
    @Nullable
    public static ItemStack getItemFromID(@Nonnull String id, int amount) {

        SlimefunItem sfItem = SlimefunItem.getByID(id);

        if (sfItem != null) {

            return new CustomItem(sfItem.getItem(), amount);

        } else {

            String changedID = id;

            if (id.equals("MC_COPPER_INGOT")) {
                changedID = "COPPER_INGOT";
            }

            Material material = Material.getMaterial(changedID);

            if (material != null){

                return new ItemStack(material, amount);

            }
        }

        return null;
    }
    
    public static void removeEnchants(@Nonnull ItemStack item) {
        for (Enchantment e : item.getEnchantments().keySet()) {
            item.removeEnchantment(e);
        }
    }
    
}
