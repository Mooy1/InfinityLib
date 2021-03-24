package io.github.mooy1.infinitylib.slimefun.utils;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class SlimefunConstants {
    
    public static final int TICKER_TICKS = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");
    public static final double TICKER_TPS = 20D / TICKER_TICKS;
    
}
