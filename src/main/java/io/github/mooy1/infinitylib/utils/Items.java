package io.github.mooy1.infinitylib.utils;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.nms.ItemNameAdapter;
import io.github.thebusybiscuit.slimefun4.libraries.dough.versions.MinecraftVersion;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
@ParametersAreNonnullByDefault
public final class Items {

    private static final NamespacedKey ID_KEY = Slimefun.getItemDataService().getKey();
    private static final Function<Object, String> TO_STRING;
    private static final Function<Object, Object> GET_NAME;
    private static final Function<ItemStack, Object> COPY;

    @Nonnull
    public static String getIdOrType(@Nonnull ItemStack item) {
        String id = getId(item);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    @Nonnull
    public static ItemStack[] arrayFrom(DirtyChestMenu menu, int[] slots) {
        ItemStack[] arr = new ItemStack[slots.length];
        for (int i = 0 ; i < arr.length ; i++) {
            arr[i] = menu.getItemInSlot(slots[i]);
        }
        return arr;
    }

    @Nonnull
    public static String getIdOrType(ItemStack item, ItemMeta meta) {
        String id = getId(meta);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

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

    @Nullable
    public static ItemStack fromId(String id) {
        return fromId(id, 1);
    }

    @Nullable
    public static ItemStack fromId(String id, int amount) {
        SlimefunItem sfItem = SlimefunItem.getById(id);
        if (sfItem != null) {
            return new CustomItemStack(sfItem.getItem(), amount);
        } else {
            return null;
        }
    }

    @Nullable
    public static ItemStack fromIdOrType(String id) {
        return fromIdOrType(id, 1);
    }

    @Nullable
    public static ItemStack fromIdOrType(String id, int amount) {
        SlimefunItem sfItem = SlimefunItem.getById(id);
        if (sfItem != null) {
            return new CustomItemStack(sfItem.getItem(), amount);
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
    public static ItemStack addLore(ItemStack item, String... lines) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore;

        if (meta.hasLore()) {
            lore = meta.getLore();
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

    public static String getName(ItemStack item) {
        return TO_STRING.apply(GET_NAME.apply(COPY.apply(item)));
    }

    public static String getName(ItemStack item, ItemMeta meta) {
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return getName(item);
    }

    // TODO pr to dough
    static {
        Function<Object, String> toString;
        Function<Object, Object> getName;
        Function<ItemStack, Object> copy;
        ItemNameAdapter adapter = ItemNameAdapter.get();
        if (adapter == null || MinecraftVersion.isMocked()) {
            toString = obj -> "TESTING";
            getName = obj -> obj;
            copy = obj -> obj;
        } else try {
            Class<?> clazz = adapter.getClass();
            toString = lambdaMethodInField(clazz, "toString");
            getName = lambdaMethodInField(clazz, "getName");
            copy = lambdaMethodInField(clazz, "copy");
        } catch (Throwable e) {
            e.printStackTrace();
            toString = obj -> "ERROR";
            getName = obj -> obj;
            copy = obj -> obj;
        }
        TO_STRING = toString;
        GET_NAME = getName;
        COPY = copy;
    }

    @SuppressWarnings("unchecked")
    private static <T, V> Function<T, V> lambdaMethodInField(Class<?> clazz, String name) throws Throwable {
        // Reflect to access the reflected method
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        Method method = (Method) field.get(null);
        method.setAccessible(true);

        // Create lambda from method
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle handle = lookup.unreflect(method);
        return (Function<T, V>) LambdaMetafactory.metafactory(
                lookup, "apply", MethodType.methodType(Function.class),
                MethodType.methodType(Object.class, Object.class),
                handle, handle.type()
        ).getTarget().invokeExact();
    }

}
