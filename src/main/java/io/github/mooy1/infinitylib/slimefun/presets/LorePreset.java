package io.github.mooy1.infinitylib.slimefun.presets;

import io.github.mooy1.infinitylib.slimefun.utils.TickerUtils;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

/**
 * Collection of utils for building item lore
 *
 * @author Mooy1
 * 
 */
@UtilityClass
public final class LorePreset {
    
    @Nonnull
    public static String energyPerSecond(int energy) {
        return "&8\u21E8 &e\u26A1 &7" + roundHundreds(energy * TickerUtils.TPS) + " J/s";
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
    public static String roundHundreds(double number) {
        return format(Math.round(number * 100D) / 100D);
    }

    private static final DecimalFormat FORMAT = new DecimalFormat("###,###,###,###,###,###.#");

    @Nonnull
    public static String format(double number) {
        return FORMAT.format(number);
    }
    
}
