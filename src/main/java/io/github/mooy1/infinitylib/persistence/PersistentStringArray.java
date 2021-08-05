package io.github.mooy1.infinitylib.persistence;

import javax.annotation.Nonnull;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

final class PersistentStringArray implements PersistentDataType<String, String[]> {

    @Nonnull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Nonnull
    @Override
    public Class<String[]> getComplexType() {
        return String[].class;
    }

    @Nonnull
    @Override
    public String toPrimitive(@Nonnull String[] complex, @Nonnull PersistentDataAdapterContext context) {
        StringBuilder builder = new StringBuilder();
        for (String string : complex) {
            builder.append(string).append(';');
        }
        return builder.toString();
    }

    @Nonnull
    @Override
    public String[] fromPrimitive(@Nonnull String primitive, @Nonnull PersistentDataAdapterContext context) {
        if (primitive.isEmpty()) {
            return new String[0];
        }
        return PatternUtils.SEMICOLON.split(primitive);
    }

}
