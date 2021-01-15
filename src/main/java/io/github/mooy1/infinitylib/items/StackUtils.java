package io.github.mooy1.infinitylib.items;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
public final class StackUtils {

    @Nullable
    public static String getID(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }
        return SlimefunPlugin.getItemDataService().getItemData(item).orElse(null);
    }
    
    @Nonnull
    public static String getIDorElse(@Nonnull ItemStack item, @Nonnull String orElse) {
        String id = getID(item);
        if (id == null) {
            return orElse;
        } else {
            return id;
        }
    }
    
    @Nullable
    public static ItemStack getItemFromID(@Nullable String id, int amount) {
        if (id == null) {
            return null;
        }
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
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
