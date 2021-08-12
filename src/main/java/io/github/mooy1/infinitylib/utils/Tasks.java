package io.github.mooy1.infinitylib.utils;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.mooy1.infinitylib.core.AbstractAddon;

/**
 * A class for registering tasks
 *
 * @author Mooy1
 */
@UtilityClass
@ParametersAreNonnullByDefault
public final class Tasks {

    private static final BukkitScheduler scheduler = Bukkit.getScheduler();

    public static void run(Runnable runnable) {
        scheduler.runTask(AbstractAddon.instance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        scheduler.runTaskAsynchronously(AbstractAddon.instance(), runnable);
    }

    public static void run(int delayTicks, Runnable runnable) {
        scheduler.runTaskLater(AbstractAddon.instance(), runnable, delayTicks);
    }

    public static void runAsync(int delayTicks, Runnable runnable) {
        scheduler.runTaskLaterAsynchronously(AbstractAddon.instance(), runnable, delayTicks);
    }

    public static void repeat(int intervalTicks, Runnable runnable) {
        repeat(intervalTicks, 0, runnable);
    }

    public static void repeatAsync(int intervalTicks, Runnable runnable) {
        repeatAsync(intervalTicks, 0, runnable);
    }

    public static void repeat(int intervalTicks, int delayTicks, Runnable runnable) {
        scheduler.runTaskTimer(AbstractAddon.instance(), runnable, delayTicks, intervalTicks);
    }

    public static void repeatAsync(int intervalTicks, int delayTicks, Runnable runnable) {
        scheduler.runTaskTimerAsynchronously(AbstractAddon.instance(), runnable, delayTicks, intervalTicks);
    }

}
