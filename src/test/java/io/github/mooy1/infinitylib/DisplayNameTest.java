package io.github.mooy1.infinitylib;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayNameTest {

    private static final ItemStack noDisplayName = new ItemStack(Material.GOLDEN_APPLE);
    private static final ItemStack hasDisplayName = new ItemStack(Material.GOLDEN_APPLE);
    private static final Method getCopyMethod;
    private static final Method getNameMethod;
    private static final Method toStringMethod;
    private static final MethodHandle getCopyMethodHandle;
    private static final MethodHandle getNameMethodHandle;
    private static final MethodHandle toStringMethodHandle;
    private static final Function<ItemStack, Object> getCopyLambda;
    private static final Function<Object, Object> getNameLambda;
    private static final Function<Object, String> toStringLambda;

    static {
        ItemMeta meta = hasDisplayName.getItemMeta();
        meta.setDisplayName("Custom Name ");
        hasDisplayName.setItemMeta(meta);

        Method getCopyMethodTemp;
        Method getNameMethodTemp;
        Method toStringMethodTemp;
        MethodHandle getCopyMethodHandleTemp;
        MethodHandle getNameMethodHandleTemp;
        MethodHandle toStringMethodHandleTemp;
        Function<ItemStack, Object> getCopyLambdaTemp;
        Function<Object, Object> getNameLambdaTemp;
        Function<Object, String> toStringLambdaTemp;

        try {
            // Methods
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String versionSpecificPackage = packageName.substring(packageName.lastIndexOf('.') + 1);
            getCopyMethodTemp = Class.forName("org.bukkit.craftbukkit." + versionSpecificPackage + ".inventory.CraftItemStack")
                    .getMethod("asNMSCopy", ItemStack.class);
            getNameMethodTemp = Class.forName("net.minecraft.world.item.ItemStack")
                    .getMethod("getName");
            toStringMethodTemp = Class.forName("net.minecraft.network.chat.IChatBaseComponent")
                    .getMethod("getString");

            getCopyMethodTemp.setAccessible(true);
            getNameMethodTemp.setAccessible(true);
            toStringMethodTemp.setAccessible(true);

            // Handles
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            getCopyMethodHandleTemp = lookup.unreflect(getCopyMethodTemp);
            getNameMethodHandleTemp = lookup.unreflect(getNameMethodTemp);
            toStringMethodHandleTemp = lookup.unreflect(toStringMethodTemp);

            // Lambdas
            getCopyLambdaTemp = createLambda(lookup, getCopyMethodHandleTemp);
            getNameLambdaTemp = createLambda(lookup, getNameMethodHandleTemp);
            toStringLambdaTemp = createLambda(lookup, toStringMethodHandleTemp);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        getCopyMethod = getCopyMethodTemp;
        getNameMethod = getNameMethodTemp;
        toStringMethod = toStringMethodTemp;
        getCopyMethodHandle = getCopyMethodHandleTemp;
        getNameMethodHandle = getNameMethodHandleTemp;
        toStringMethodHandle = toStringMethodHandleTemp;
        getCopyLambda = getCopyLambdaTemp;
        getNameLambda = getNameLambdaTemp;
        toStringLambda = toStringLambdaTemp;
    }

    @SuppressWarnings("unchecked")
    private static <T, V> Function<T, V> createLambda(MethodHandles.Lookup lookup, MethodHandle handle) throws Throwable {
        return (Function<T, V>) LambdaMetafactory.metafactory(
                lookup, "apply",
                MethodType.methodType(Function.class),
                MethodType.methodType(Object.class, Object.class),
                handle, handle.type()
        ).getTarget().invokeExact();
    }

    public static void test() {
        System.out.println("| ----------------------------------------------------------------------------------------- |");
        System.out.println("| Testing methods of getting the display name of ItemStacks (Paper 1.17.1)                  |");
        System.out.println("| ----------------------------------------------------------------------------------------- |");
        System.out.println("| Test                | Item has display name? | Display Name | Average time in nanoseconds |");
        test("getDisplayName()   ", true, () -> { //
            return hasDisplayName.getItemMeta().getDisplayName();
        });
        test("Method Invoke      ", true, () -> {
            try {
                return (String) toStringMethod.invoke(getNameMethod.invoke(getCopyMethod.invoke(null, hasDisplayName)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        test("MethodHandle Invoke", true, () -> {
            try {
                return (String) toStringMethodHandle.invoke(getNameMethodHandle.invoke(getCopyMethodHandle.invoke(hasDisplayName)));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
        test("Lambda Function    ", true, () -> { //
            return toStringLambda.apply(getNameLambda.apply(getCopyLambda.apply(hasDisplayName)));
        });
        test("Method Invoke      ", false, () -> {
            try {
                return (String) toStringMethod.invoke(getNameMethod.invoke(getCopyMethod.invoke(null, noDisplayName)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        test("MethodHandle Invoke", false, () -> {
            try {
                return (String) toStringMethodHandle.invoke(getNameMethodHandle.invoke(getCopyMethodHandle.invoke(noDisplayName)));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
        test("Lambda Function    ", false, () -> { //
            return toStringLambda.apply(getNameLambda.apply(getCopyLambda.apply(noDisplayName)));
        });
        System.out.println("| ----------------------------------------------------------------------------------------- |");
    }

    private static void test(String name, boolean hasDisplayName, Supplier<String> supplier) {
        long time = System.nanoTime();
        for (int i = 0; i < 1_000; i++) {
            supplier.get();
        }
        time = System.nanoTime() - time;
        String itemHasDisplayName = hasDisplayName ? "True                  " : "False                 ";
        System.out.println("| " + name + " | " + itemHasDisplayName + " | " + supplier.get() + " | " + (time / 1_000D));
    }

}
