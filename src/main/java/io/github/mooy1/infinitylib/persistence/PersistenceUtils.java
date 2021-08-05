package io.github.mooy1.infinitylib.persistence;

import java.util.List;

import lombok.experimental.UtilityClass;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@UtilityClass
public final class PersistenceUtils {

    public static final PersistentDataType<String, ItemStack> ITEM_STACK = new PersistentItemStack();
    public static final PersistentDataType<byte[], List<ItemStack>> ITEM_STACK_LIST = new PersistentStackList();
    public static final PersistentDataType<byte[], Location> LOCATION = new PersistentLocation();
    public static final PersistentDataType<byte[], List<String>> STRING_LIST = new PersistentStringList();

}
