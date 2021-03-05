package io.github.mooy1.infinitylib.items;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Persistent data type for ItemStacks
 * 
 * @author Mooy1
 */
public final class PersistentItemStack implements PersistentDataType<String, ItemStack> {

    public static final PersistentDataType<String, ItemStack> TYPE = new PersistentItemStack();
    
    private PersistentItemStack() {}
    
    @Nonnull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Nonnull
    @Override
    public Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @Nonnull
    @Override
    public String toPrimitive(@Nonnull ItemStack complex, @Nonnull PersistentDataAdapterContext context) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", complex);
        return config.saveToString();
    }
    
    @Nonnull
    @Override
    public ItemStack fromPrimitive(@Nonnull String primitive, @Nonnull PersistentDataAdapterContext context) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(primitive);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(config.getItemStack("item"));
    }

}
