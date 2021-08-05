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
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (BukkitObjectOutputStream output = new BukkitObjectOutputStream(bytes)) {
            for (ItemStack item : complex) {
                output.writeObject(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes.toByteArray();
    }

    @Nonnull
    @Override
    public List<ItemStack> fromPrimitive(@Nonnull byte[] primitive, @Nonnull PersistentDataAdapterContext context) {
        ByteArrayInputStream bytes = new ByteArrayInputStream(primitive);
        List<ItemStack> list = new ArrayList<>();
        try (BukkitObjectInputStream input = new BukkitObjectInputStream(bytes)) {
            while (bytes.available() > 0) {
                list.add((ItemStack) input.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
