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
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
public final class StackUtils {

    @Nullable
    @Contract("null -> null")
    public static String getID(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }
        return SlimefunPlugin.getItemDataService().getItemData(item).orElse(null);
    }
    
    @Contract("null, _ -> null ; ")
    public static String getIDorElse(@Nullable ItemStack item, @Nullable String orElse) {
        if (item == null) {
            return null;
        }
        String id = getID(item);
        if (id == null) {
            return orElse;
        } else {
            return id;
        }
    }
    
    @Nullable
    @Contract("null, _ -> null")
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

    @Contract("null -> null ; !null -> !null")
    public static ItemStack getUnique(ItemStack item) {
        if (item == null) {
            return null;
        } else {
            return makeUnique(item.clone());
        }
    }

    @Contract("_ -> param1")
    public static ItemStack makeUnique(ItemStack item) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    @Contract("_ -> param1")
    public static ItemStack removeEnchants(ItemStack item) {
        if (item != null) {
            for (Enchantment e : item.getEnchantments().keySet()) {
                item.removeEnchantment(e);
            }
        }
        return item;
    }
    
}
