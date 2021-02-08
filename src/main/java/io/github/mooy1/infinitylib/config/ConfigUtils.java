package io.github.mooy1.infinitylib.config;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.logging.Level;

@UtilityClass
public final class ConfigUtils {

    private static final ConfigurationSection CONFIG = PluginUtils.getPlugin().getConfig();
    
    public static int getInt(String path, int min, int max, int def) {
        return getInt(CONFIG, path, min, max, def);
    }

    public static boolean getBoolean(String path, boolean def) {
        return getBoolean(CONFIG, path, def);
    }

    public static double getDouble(String path, double min, double max, double def) {
        return getDouble(CONFIG, path, min, max, def);
    }

    public static String getString(String path, String def) {
        return getString(CONFIG, path, def);
    }
    
    public static int getInt(ConfigurationSection section, String path, int min, int max, int def) {
        int value = section.getInt(path);
        if (value >= min && value <= max) {
            return value;
        } else {
            configWarnValue(path);
            section.set(path, def);
            PluginUtils.getPlugin().saveConfig();
            return def;
        }
    }

    public static boolean getBoolean(ConfigurationSection section, String path, boolean def) {
        String value = section.getString(path);
        if (Objects.equals(value, "true")) {
            return true;
        } else if (Objects.equals(value, "false")) {
            return false;
        } else {
            configWarnValue(path);
            section.set(path, def);
            PluginUtils.getPlugin().saveConfig();
            return def;
        }
    }

    public static double getDouble(ConfigurationSection section, String path, double min, double max, double def) {
        double value = section.getDouble(path);
        if (value >= min && value <= max) {
            return value;
        } else {
            configWarnValue(path);
            section.set(path, def);
            PluginUtils.getPlugin().saveConfig();
            return def;
        }
    }

    public static String getString(ConfigurationSection section, String path, String def) {
        String value = section.getString(path);
        if (value != null) {
            return value;
        } else {
            configWarnValue(path);
            section.set(path, def);
            PluginUtils.getPlugin().saveConfig();
            return def;
        }
    }
    
    public static void configWarnValue(String path) {
        PluginUtils.log(Level.WARNING, "Config value of " + path + " was out of bounds, resetting it to default");
    }
    
}
