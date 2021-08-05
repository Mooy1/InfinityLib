package io.github.mooy1.infinitylib.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

final class PersistentStringList implements PersistentDataType<byte[], List<String>> {

    @Nonnull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Nonnull
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Class<List<String>> getComplexType() {
        return (Class) List.class;
    }

    @Nonnull
    @Override
    public byte[] toPrimitive(@Nonnull List<String> complex, @Nonnull PersistentDataAdapterContext context) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(complex.size() * 20);
            for (String string : complex) {
                byte[] arr = string.getBytes();
                bytes.write(arr.length);
                bytes.write(arr, 0, arr.length);
            }
            return bytes.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Nonnull
    @Override
    public List<String> fromPrimitive(@Nonnull byte[] primitive, @Nonnull PersistentDataAdapterContext context) {
        try {
            ByteArrayInputStream bytes = new ByteArrayInputStream(primitive);
            List<String> list = new ArrayList<>(primitive.length / 20);
            while (bytes.available() > 0) {
                byte[] arr = new byte[bytes.read()];
                bytes.read(arr, 0, arr.length);
                list.add(new String(arr));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
