package io.github.mooy1.infinitylib.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
// TODO rename
@UtilityClass
@ParametersAreNonnullByDefault
public final class ItemStacks {

    private static final NamespacedKey ID_KEY = Slimefun.getItemDataService().getKey();

    // TODO move to recipes
    @Nonnull
    public static String getIdOrType(@Nonnull ItemStack item) {
        String id = getId(item);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    // TODO move to recipes
    @Nonnull
    public static ItemStack[] arrayFrom(DirtyChestMenu menu, int[] slots) {
        ItemStack[] arr = new ItemStack[slots.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = menu.getItemInSlot(slots[i]);
        }
        return arr;
    }

    // TODO move to recipes
    @Nonnull
    public static String getIdOrType(ItemStack item, ItemMeta meta) {
        String id = getId(meta);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    // TODO move to SFUtils
    @Nullable
    public static String getId(ItemStack item) {
        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }
        return item.hasItemMeta() ? getId(item.getItemMeta()) : null;
    }

    @Nullable
    public static String getId(ItemMeta meta) {
        return meta.getPersistentDataContainer().get(ID_KEY, PersistentDataType.STRING);
    }

}
