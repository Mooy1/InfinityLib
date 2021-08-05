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
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(complex.size() * 20);
        for (String string : complex) {
            byte[] arr = string.getBytes();
            bytes.write(arr.length);
            bytes.write(arr, 0, arr.length);
        }
        return bytes.toByteArray();
    }

    @Nonnull
    @Override
    public List<String> fromPrimitive(@Nonnull byte[] primitive, @Nonnull PersistentDataAdapterContext context) {
        ByteArrayInputStream bytes = new ByteArrayInputStream(primitive);
        List<String> list = new ArrayList<>(primitive.length / 20);
        try {
            while (bytes.available() > 0) {
                byte[] arr = new byte[bytes.read()];
                bytes.read(arr, 0, arr.length);
                list.add(new String(arr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
