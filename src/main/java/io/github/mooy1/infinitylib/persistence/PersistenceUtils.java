package io.github.mooy1.infinitylib.persistence;

import lombok.experimental.UtilityClass;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@UtilityClass
public final class PersistenceUtils {

    public static final PersistentDataType<String, ItemStack> ITEM_STACK = new PersistentItemStack();
    public static final PersistentDataType<String, ItemStack[]> STACK_ARRAY = new PersistentStackArray();
    public static final PersistentDataType<String, Block> BLOCK = new PersistentBlock();
    
}
