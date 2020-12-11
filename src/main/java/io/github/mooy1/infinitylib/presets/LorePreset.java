package io.github.mooy1.infinitylib.presets;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

/**
 * Collection of utils for building item lore
 *
 * @author Mooy1
 * 
 */
public final class LorePreset {
    
    public static final float TICK_RATIO = 20F / SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    
    public static String energyPerSecond(int energy) {
        return "&8\u21E8 &e\u26A1 &7" + format(Math.round(energy * TICK_RATIO)) + " J/s";
    }
    
    @Nonnull
    public static String energyBuffer(int energy) {
        return "&8\u21E8 &e\u26A1 &7" + format(energy) + " J Buffer";
    }

    @Nonnull
    public static String energy(int energy) {
        return "&8\u21E8 &e\u26A1 &7" + format(energy) + " J ";
    }

    @Nonnull
    public static String speed(int speed) {
        return "&8\u21E8 &b\u26A1 &7Speed: &b" + speed + 'x';
    }

    @Nonnull
    public static String storesItem(int amount) {
        return "&6Capacity: &e" + format(amount) + " &eitems";
    }

    @Nonnull
    public static String storesInfinity() {
        return "&6Capacity:&e infinite items";
    }

    @Nonnull
    public static String roundHundreds(float number) {
        return format(Math.round(number * 100) / 100);
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");

    @Nonnull
    public static String format(int number) {
        return decimalFormat.format(number);
    }
    
}
