package io.github.mooy1.infinitylib.persistence;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

final class PersistentLocation implements PersistentDataType<String, Location> {

    @Nonnull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Nonnull
    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @Nonnull
    @Override
    public String toPrimitive(@Nonnull Location complex, @Nonnull PersistentDataAdapterContext context) {
        return (complex.getWorld() != null ? complex.getWorld().getName() : "NULL") + ";" + complex.getX() + ";" + complex.getY() + ";" + complex.getZ();
    }

    @Nonnull
    @Override
    public Location fromPrimitive(@Nonnull String primitive, @Nonnull PersistentDataAdapterContext context) {
        String[] strings = PatternUtils.SEMICOLON.split(primitive);
        return new Location(
                Bukkit.getWorld(strings[0]),
                Double.parseDouble(strings[1]),
                Double.parseDouble(strings[2]),
                Double.parseDouble(strings[3])
        );
    }

}
