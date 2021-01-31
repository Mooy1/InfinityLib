package io.github.mooy1.infinitylib;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.logging.Level;

@UtilityClass
public final class ConfigUtils {
    
    private static final FileConfiguration config = PluginUtils.getPlugin().getConfig();
    
    public static int getOrDefault(String path, int min, int max, int def) {
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

    public static boolean getOrDefault(String path, boolean def) {
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

    public static double getOrDefault(String path, double min, double max, double def) {
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

    public static String getOrDefault(String path, String def) {
        String value = config.getString(path);
        if (value != null) {
            return value;
        } else {
            configWarnValue(path);
            config.set(path, def);
            PluginUtils.getPlugin().saveConfig();
            return def;
        }
    }
    
    public static void configWarnValue(String path) {
        PluginUtils.log(Level.WARNING, "Config value at " + path + " was out of bounds, resetting it to default");
    }
    
}
