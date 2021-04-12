package io.github.mooy1.infinitylib.slimefun.utils;

import lombok.experimental.UtilityClass;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

@UtilityClass
public final class TickerUtils {
    
    public static final int TICKS = SlimefunPlugin.getTickerTask().getTickRate();
    public static final double TPS = 20D / TICKS;
    
}
