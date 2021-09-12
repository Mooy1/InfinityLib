package io.github.mooy1.infinitylib.common;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

import org.bukkit.configuration.file.YamlConfiguration;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.core.Environment;

@UtilityClass
public final class Translations {

    static final YamlConfiguration DEFAULTS;
    static final YamlConfiguration CONFIG;
    static final File FILE;

    static {
        FILE = new File(AbstractAddon.instance().getDataFolder(), "translations.yml");
        CONFIG = YamlConfiguration.loadConfiguration(FILE);
        DEFAULTS = new YamlConfiguration();
        CONFIG.setDefaults(DEFAULTS);
        CONFIG.options().copyDefaults(true);

        if (AbstractAddon.environment() == Environment.LIVE) {
            Scheduler.runAsync(() -> {
                try {
                    CONFIG.save(FILE);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * You should cache the result of this method in a static or instance variable
     */
    @Nonnull
    public static String get(String key, String defaultString) {
        DEFAULTS.set(key, defaultString);
        return CONFIG.getString(key, defaultString);
    }

}
