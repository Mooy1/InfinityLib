package io.github.mooy1.infinitylib;

import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
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
    public static Config loadConfig(@Nonnull String resource) {
        Config config = new Config(PluginUtils.getPlugin(), resource);
        attachDefaults(config, resource);
        return config;
    }
    
    @Nonnull
    public static Config loadConfig(@Nonnull File folder, @Nonnull String name) {
        return new Config(new File(folder, name));
    }

    @Nonnull
    public static Config loadConfig(@Nonnull File folder, @Nonnull String name, @Nonnull String resource) {
        Config config = new Config(new File(folder, name));
        attachDefaults(config, resource);
        return config;
    }
    
    public static void attachDefaults(@Nonnull Config config, @Nonnull String resource) {
        InputStream defaultResource = PluginUtils.getPlugin().getResource(resource);

        Objects.requireNonNull(defaultResource, () -> "Failed to get default resource " + resource + "!");

        config.getConfiguration().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultResource)));

        config.getConfiguration().options().copyDefaults(true).copyHeader(true);

        config.save();
    }
    
    private static final ConfigurationSection CONFIG = PluginUtils.getPlugin().getConfig();
    
    public static String getString(String path) {
        return CONFIG.getString(path);
    }

    public static boolean getBoolean(String path) {
        return CONFIG.getBoolean(path);
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
