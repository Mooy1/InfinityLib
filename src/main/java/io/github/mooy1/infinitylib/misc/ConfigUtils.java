package io.github.mooy1.infinitylib.misc;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.logging.Level;

@UtilityClass
public final class ConfigUtils {
    
    @Setter
    private static FileConfiguration config;
    
    public static int getOrDefault(String path, int min, int max, int def) {
        if (hasPath(path)) {
            int value = config.getInt(path);
            if (value >= min && value <= max) {
                return value;
            } else {
                configWarnValue(path);
                config.set(path, def);
                PluginUtils.getPlugin().saveConfig();
                return def;
            }
        }
        return def;
    }

    public static boolean getOrDefault(String path, boolean def) {
        if (hasPath(path)) {
            String value = config.getString(path);
            if (Objects.equals(value, "true")) {
                return true;
            } else if (Objects.equals(value, "false")) {
                return false;
            } else {
                configWarnValue(path);
                config.set(path, def);
                PluginUtils.getPlugin().saveConfig();
                return def;
            }
        }
        return def;
    }

    public static double getOrDefault(String path, double min, double max, double def) {
        if (hasPath(path)) {
            double value = config.getDouble(path);
            if (value >= min && value <= max) {
                return value;
            } else {
                configWarnValue(path);
                config.set(path, def);
                PluginUtils.getPlugin().saveConfig();
                return def;
            }
        }
        return def;
    }

    private static boolean hasPath(String path) {
        Validate.notNull(config, "You must set the config before using config utils");
        if (config.contains(path)) {
            return true;
        } else {
            configWarnPath(path);
            return false;
        }
    }

    public static void configWarnValue(String path) {
        PluginUtils.log(Level.WARNING, "Config value at " + path + " was out of bounds, resetting it to default");
    }

    public static void configWarnPath(String path) {
        PluginUtils.log(Level.SEVERE, "Config was missing path " + path + ", please add this path or reset your config!");
    }
    
}
