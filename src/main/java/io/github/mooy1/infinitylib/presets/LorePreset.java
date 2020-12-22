package io.github.mooy1.infinitylib.presets;

import io.github.mooy1.infinitylib.PluginUtils;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

/**
 * Collection of utils for building item lore
 *
 * @author Mooy1
 * 
 */
public final class LorePreset {
    
    @Nonnull
    public static String energyPerSecond(int energy) {
        return "&8\u21E8 &e\u26A1 &7" + format(Math.round(energy * PluginUtils.TICK_RATIO)) + " J/s";
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
    public static String romanNumeral(int number) {
        switch(number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            case 10: return "X";
        }
        return "OUT OF BOUNDS";
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
