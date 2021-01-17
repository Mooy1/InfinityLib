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
    
    @Nullable
    public static String getNullableIDorElse(@Nullable ItemStack item, @Nullable String orElse) {
        if (item == null) {
            return null;
        }
        return getIDorElse(item, orElse);
    }
    
    @Nonnull
    public static String getIDorElse(@Nonnull ItemStack item, String orElse) {
        String id = getID(item);
        if (id == null) {
            return orElse;
        } else {
            return id;
        }
    }
    
    @Nullable
    public static ItemStack getItemByNullableID(@Nullable String id) {
        return getItemByNullableID(id, 1);
    }

    @Nullable
    public static ItemStack getItemByNullableID(@Nullable String id, int amount) {
        if (id == null) {
            return null;
        }
        return getItemByID(id, amount);
    }
    
    @Nonnull
    public static ItemStack getItemByID(@Nonnull String id) {
        return getItemByID(id, 1);
    }

    @Nonnull
    public static ItemStack getItemByID(@Nonnull String id, int amount) {
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
            Material material = Material.getMaterial(id);
            if (material != null){
                return new ItemStack(material, amount);
            } else {
                throw new IllegalArgumentException("Item ID " + id + " does not correspond to any item!");
            }
        }
    }

    private static final NamespacedKey key = PluginUtils.getKey("unique_item");
    
    @Nonnull
    public static ItemStack getUnique(@Nonnull ItemStack item) {
        return makeUnique(item.clone());
    }

    @Nonnull
    public static ItemStack makeUnique(@Nonnull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    @Nonnull
    public static ItemStack removeEnchants(@Nonnull ItemStack item) {
        for (Enchantment e : item.getEnchantments().keySet()) {
            item.removeEnchantment(e);
        }
        return item;
    }
    
}
