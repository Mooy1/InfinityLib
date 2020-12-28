package io.github.mooy1.infinitylib.items;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.NonNull;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

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
            String id = item.getType().toString();
            
            if (id.equals("COPPER_INGOT")) {
                return "MC_COPPER_INGOT";
            }
            
            return id;
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
            
            if (id.equals("MC_COPPER_INGOT")) {
                id = "COPPER_INGOT";
            }

            Material material = Material.getMaterial(id);

            if (material != null){

                return new ItemStack(material, amount);

            }
        }

        return null;
    }

    private static final NamespacedKey key = PluginUtils.getKey("unique_item");

    @Nonnull
    public static ItemStack getUnique(@Nonnull ItemStack item) {
        return makUnique(item.clone());
    }

    @Nonnull
    public static ItemStack makUnique(@Nonnull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    public static void removeEnchants(@Nonnull ItemStack item) {
        for (Enchantment e : item.getEnchantments().keySet()) {
            item.removeEnchantment(e);
        }
    }
    
}
