package io.github.mooy1.infinitylib;

import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public final class PluginUtils {

    @Setter
    private static Plugin plugin = null;

    public static void setupConfig() {
        validate();
        
        plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true).copyHeader(true);
        plugin.saveConfig();
    }

    public static void log(@Nonnull Level level , @Nonnull String... logs) {
        validate();
        
        for (String log : logs) {
            plugin.getLogger().log(level, log);
        }
    }
    
    public static void runSync(@Nonnull Runnable runnable, long delay) {
        validate();
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static void runSync(@Nonnull Runnable runnable) {
        validate();
        Validate.notNull(runnable, "Cannot run null");

        if (!plugin.isEnabled()) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    private static void validate() {
        Validate.notNull(plugin, "Make sure to set the plugin instance");
    }
    
}
