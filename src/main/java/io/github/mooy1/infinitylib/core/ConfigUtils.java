package io.github.mooy1.infinitylib.core;

import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Utility class for reading from config
 * 
 * @author Mooy1
 */
@UtilityClass
public final class ConfigUtils {

    @Nonnull
    public static Config load(@Nonnull String resource) {
        return attachDefaults(new Config(PluginUtils.getPlugin(), resource), getDefaults(resource));
    }
    
    @Nonnull
    public static Config load(@Nonnull File folder, @Nonnull String name) {
        return new Config(new File(folder, name));
    }

    @Nonnull
    public static Config loadWithDefaults(@Nonnull File file, @Nonnull Configuration defaults) {
        return attachDefaults(new Config(file), defaults);
    }

    @Nonnull
    public static Config loadWithDefaults(@Nonnull File folder, @Nonnull String name, @Nonnull Configuration defaults) {
        return attachDefaults(new Config(new File(folder, name)), defaults);
    }
    
    public static Config attachDefaults(@Nonnull Config config, @Nonnull Configuration defaults) {
        config.getConfiguration().setDefaults(defaults);
        config.getConfiguration().options().copyDefaults(true).copyHeader(true);
        config.save();
        return config;
    }
    
    public static Configuration getDefaults(@Nonnull String resource) {
        return YamlConfiguration.loadConfiguration(new InputStreamReader(
                        Objects.requireNonNull(PluginUtils.getPlugin().getResource(resource),
                                () -> "Failed to get default resource " + resource + "!")));
    }
    
    private static final ConfigurationSection CONFIG = PluginUtils.getPlugin().getConfig();
    
    public static String getString(String path, String def) {
        return CONFIG.getString(path, def);
    }

    public static boolean getBoolean(String path, boolean def) {
        return CONFIG.getBoolean(path, def);
    }
    
    public static int getInt(String path, int min, int max, int def) {
        return getInt(CONFIG, path, min, max, def);
    }

    public static double getDouble(String path, double min, double max, double def) {
        return getDouble(CONFIG, path, min, max, def);
    }
    
    public static int getInt(ConfigurationSection section, String path, int min, int max, int def) {
        int value = section.getInt(path);
        if (value >= min && value <= max) {
            return value;
        } else {
            configWarnValue(section, path);
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
            configWarnValue(section, path);
            section.set(path, def);
            PluginUtils.getPlugin().saveConfig();
            return def;
        }
    }
    
    public static void configWarnValue(ConfigurationSection section, String path) {
        PluginUtils.log(Level.WARNING, "Config value of " + section.getCurrentPath() + '.' + path + " was out of bounds, resetting it to default");
    }
    
}
