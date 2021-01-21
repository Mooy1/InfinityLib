package io.github.mooy1.infinitylib.items;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
public final class StackUtils {

    private static final NamespacedKey dataKey = SlimefunPlugin.getItemDataService().getKey();
    
    @Nullable
    public static String getIDofNullable(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        return getID(item);
    }

    @Nullable
    public static String getIDorTypeOfNullable(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        return getIDorType(item);
    }

    @Nullable
    public static String getID(@Nonnull ItemStack item) {
        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }
        return getID(item.getItemMeta().getPersistentDataContainer());
    }
    
    @Nonnull
    public static String getIDorType(@Nonnull ItemStack item) {
        String id = getID(item);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }
    
    @Nullable
    public static String getID(@Nonnull PersistentDataContainer container) {
        return container.get(dataKey, PersistentDataType.STRING);
    }

    @Nonnull
    public static String getIDorType(@Nonnull PersistentDataContainer container, @Nonnull ItemStack item) {
        String id = getID(container);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    @Nullable
    public static ItemStack getItemByNullableID(@Nullable String id) {
        if (id == null) {
            return null;
        }
        return getItemByID(id, 1);
    }

    @Nullable
    public static ItemStack getItemByNullableIDorType(@Nullable String id) throws IllegalArgumentException {
        if (id == null) {
            return null;
        }
        return getItemByIDorType(id, 1);
    }

    @Nullable
    public static ItemStack getItemByNullableID(@Nullable String id, @Range(from = 1, to = 64) int amount) {
        if (id == null) {
            return null;
        }
        return getItemByID(id, amount);
    }

    @Nullable
    public static ItemStack getItemByNullableIDorType(@Nullable String id, @Range(from = 1, to = 64) int amount) {
        if (id == null) {
            return null;
        }
        return getItemByIDorType(id, amount);
    }

    @Nullable
    public static ItemStack getItemByID(@Nonnull String id) {
        return getItemByID(id, 1);
    }

    @Nullable
    public static ItemStack getItemByID(@Nonnull String id, @Range(from = 1, to = 64) int amount) {
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
            return null;
        }
    }

    @Nullable
    public static ItemStack getItemByIDorType(@Nonnull String id) {
        return getItemByIDorType(id, 1);
    }

    @Nullable
    public static ItemStack getItemByIDorType(@Nonnull String id, @Range(from = 1, to = 64) int amount) {
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
            Material material = Material.getMaterial(id);
            if (material != null){
                return new ItemStack(material, amount);
            } else {
                return null;
            }
        }
    }
    
    @Nonnull
    public static ItemStack removeEnchants(@Nonnull ItemStack item) {
        for (Enchantment e : item.getEnchantments().keySet()) {
            item.removeEnchantment(e);
        }
        return item;
    }
    
}
