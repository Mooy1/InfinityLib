package io.github.mooy1.infinitylib.items;

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

import lombok.experimental.UtilityClass;

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
import me.mrCookieSlime.Slimefun.cscorelib2.reflection.ReflectionUtils;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
public final class StackUtils {

    private static final NamespacedKey ID_KEY = SlimefunPlugin.getItemDataService().getKey();
    private static final Function<Object, String> TO_STRING;
    private static final Function<Object, Object> GET_NAME;
    private static final Function<ItemStack, Object> COPY;

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
        return item.hasItemMeta() ? getID(item.getItemMeta()) : null;
    }

    @Nullable
    public static String getID(@Nonnull ItemMeta meta) {
        return meta.getPersistentDataContainer().get(ID_KEY, PersistentDataType.STRING);
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

    @Nonnull
    public static ItemStack[] arrayFrom(@Nonnull DirtyChestMenu menu, int[] slots) {
        ItemStack[] arr = new ItemStack[slots.length];
        for (int i = 0 ; i < arr.length ; i++) {
            arr[i] = menu.getItemInSlot(slots[i]);
        }
        return arr;
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

    public static String getDisplayName(@Nonnull ItemStack item) {
        return TO_STRING.apply(GET_NAME.apply(COPY.apply(item)));
    }

    public static String getDisplayName(@Nonnull ItemStack item, @Nonnull ItemMeta meta) {
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return getDisplayName(item);
    }

    static {
        Function<Object, String> toString;
        Function<Object, Object> getName;
        Function<ItemStack, Object> copy;
        if (ReflectionUtils.isUnitTestEnvironment()) {
            toString = obj -> "TESTING";
            getName = obj -> obj;
            copy = obj -> obj;
        } else try {
            toString = lambdaItemUtilsMethod("toString");
            getName = lambdaItemUtilsMethod("getName");
            copy = lambdaItemUtilsMethod("copy");
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
    private static <T, V> Function<T, V> lambdaItemUtilsMethod(String name) throws Throwable {
        // Reflect to access the reflected method
        Field field = ItemUtils.class.getDeclaredField(name);
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
