package io.github.mooy1.infinitylib.items;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.UtilityClass;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
public final class StackUtils {

    private static final NamespacedKey KEY = SlimefunPlugin.getItemDataService().getKey();

    @Nonnull
    public static String getIDorType(@Nonnull ItemStack item) {
        String id = getID(item);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    @Nonnull
    public static String getIDorType(@Nonnull ItemStack item, @Nonnull ItemMeta meta) {
        String id = getID(meta);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    @Nullable
    public static String getID(@Nonnull ItemStack item) {
        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }
        if (item instanceof FastItemStack) {
            return ((FastItemStack) item).getID();
        }
        if (!item.hasItemMeta()) {
            return null;
        }
        return getID(item.getItemMeta());
    }

    @Nullable
    public static String getID(@Nonnull ItemMeta meta) {
        return meta.getPersistentDataContainer().get(KEY, PersistentDataType.STRING);
    }

    @Nullable
    public static ItemStack getItemByID(@Nonnull String id) {
        return getItemByID(id, 1);
    }

    @Nullable
    public static ItemStack getItemByID(@Nonnull String id, int amount) {
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
    public static ItemStack getItemByIDorType(@Nonnull String id, int amount) {
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
            Material material = Material.getMaterial(id);
            if (material != null) {
                return new ItemStack(material, amount);
            } else {
                return null;
            }
        }
    }

    @Nonnull
    public static ItemStack addLore(@Nonnull ItemStack item, @Nonnull String... lines) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if (meta.hasLore()) {
            lore = meta.getLore();

            if (lore == null) {
                lore = new ArrayList<>();
            }
        } else {
            lore = new ArrayList<>();
        }

        for (String line : lines) {
            lore.add(ChatColors.color(line));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static Method COPY;
    private static Method GET_NAME;
    private static Method TO_STRING;

    static {
        try {
            Field field = ItemUtils.class.getDeclaredField("copy");
            field.setAccessible(true);
            COPY = (Method) field.get(null);
            COPY.setAccessible(true);
            field = ItemUtils.class.getDeclaredField("getName");
            field.setAccessible(true);
            GET_NAME = (Method) field.get(null);
            GET_NAME.setAccessible(true);
            field = ItemUtils.class.getDeclaredField("toString");
            field.setAccessible(true);
            TO_STRING = (Method) field.get(null);
            TO_STRING.setAccessible(true);
        } catch (Exception ignored) {
            // MockBukkit
        }
    }

    public static String getInternalName(@Nonnull ItemStack item) {
        try {
            return ChatColor.WHITE + (String) TO_STRING.invoke(GET_NAME.invoke(COPY.invoke(null, item)));
        } catch (Exception e) {
            return ChatColor.RED + "ERROR";
        }
    }

    public static String getDisplayName(@Nonnull ItemStack item, @Nonnull ItemMeta meta) {
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return getInternalName(item);
    }

    public static String getDisplayName(@Nonnull ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                return meta.getDisplayName();
            }
        }
        return getInternalName(item);
    }

    public static ItemStack[] arrayFrom(@Nonnull DirtyChestMenu menu, int[] slots) {
        ItemStack[] arr = new ItemStack[slots.length];
        for (int i = 0 ; i < arr.length ; i++) {
            arr[i] = menu.getItemInSlot(slots[i]);
        }
        return arr;
    }

}
