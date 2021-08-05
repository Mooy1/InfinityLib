package io.github.mooy1.infinitylib.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

/**
 * Persistent data type for ItemStack arrays
 *
 * @author Mooy1
 */
final class PersistentStackList implements PersistentDataType<byte[], List<ItemStack>> {

    @Nonnull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Nonnull
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Class<List<ItemStack>> getComplexType() {
        return (Class) List.class;
    }

    @Nonnull
    @Override
    public byte[] toPrimitive(@Nonnull List<ItemStack> complex, @Nonnull PersistentDataAdapterContext context) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            BukkitObjectOutputStream output = new BukkitObjectOutputStream(bytes);
            for (ItemStack item : complex) {
                output.writeObject(item);
            }
            return bytes.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> fromPrimitive(@Nonnull byte[] primitive, @Nonnull PersistentDataAdapterContext context) {
        try {
            ByteArrayInputStream bytes = new ByteArrayInputStream(primitive);
            BukkitObjectInputStream input = new BukkitObjectInputStream(bytes);
            List<ItemStack> list = new ArrayList<>();
            while (bytes.available() > 0) {
                list.add((ItemStack) input.readObject());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
