package io.github.mooy1.infinitylib.common;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;

import io.github.mooy1.infinitylib.core.AbstractAddon;

/**
 * A class for scheduling tasks
 *
 * @author Mooy1
 */
@UtilityClass
public final class Scheduler {

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(AbstractAddon.instance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(AbstractAddon.instance(), runnable);
    }

    public static void run(int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(AbstractAddon.instance(), runnable, delayTicks);
    }

    public static void runAsync(int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(AbstractAddon.instance(), runnable, delayTicks);
    }

    public static void repeat(int intervalTicks, Runnable runnable) {
        repeat(intervalTicks, 1, runnable);
    }

    public static void repeatAsync(int intervalTicks, Runnable runnable) {
        repeatAsync(intervalTicks, 1, runnable);
    }

    public static void repeat(int intervalTicks, int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskTimer(AbstractAddon.instance(), runnable, delayTicks, Math.max(1, intervalTicks));
    }

    public static void repeatAsync(int intervalTicks, int delayTicks, Runnable runnable) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(AbstractAddon.instance(), runnable, delayTicks, Math.max(1, intervalTicks));
    }

}
