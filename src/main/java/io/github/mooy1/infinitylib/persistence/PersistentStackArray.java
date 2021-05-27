package io.github.mooy1.infinitylib.persistence;

import javax.annotation.Nonnull;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

/**
 * Persistent data type for ItemStack arrays
 *
 * @author Mooy1
 */
final class PersistentStackArray implements PersistentDataType<String, ItemStack[]> {

    @Nonnull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Nonnull
    @Override
    public Class<ItemStack[]> getComplexType() {
        return ItemStack[].class;
    }

    @Nonnull
    @Override
    public String toPrimitive(@Nonnull ItemStack[] complex, @Nonnull PersistentDataAdapterContext context) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("length", complex.length);
        for (int i = 0 ; i < complex.length ; i++) {
            config.set(String.valueOf(i), complex[i]);
        }
        return config.saveToString();
    }

    @Nonnull
    @Override
    public ItemStack[] fromPrimitive(@Nonnull String primitive, @Nonnull PersistentDataAdapterContext context) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(primitive);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
        ItemStack[] arr = new ItemStack[config.getInt("length")];
        for (int i = 0 ; i < arr.length ; i++) {
            arr[i] = config.getItemStack(String.valueOf(i));
        }
        return arr;
    }

}
