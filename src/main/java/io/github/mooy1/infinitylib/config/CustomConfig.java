package io.github.mooy1.infinitylib.config;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

public final class CustomConfig {

    @Getter
    @Nonnull
    private final FileConfiguration config;
    
    @Getter
    @Nonnull
    private final File file;
    
    @Getter
    @Nonnull
    private final String resource;
    
    public CustomConfig(@Nonnull String resource) {
        Validate.notNull(resource);
        
        this.resource = resource;

        JavaPlugin plugin = PluginUtils.getPlugin();

        this.file = new File(plugin.getDataFolder(), resource);

        if (!this.file.exists()) {
            try {
                plugin.saveResource(resource, false);
            } catch (IllegalArgumentException e) {
                PluginUtils.log(Level.SEVERE, "Failed to save default " + resource + " file!");
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);

        InputStream defaultResource = plugin.getResource(resource);

        Objects.requireNonNull(defaultResource, () -> "Failed to get default " + resource + " file!");

        this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultResource)));

        this.config.options().copyDefaults(true).copyHeader(true);

        save();
    }
    
    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            PluginUtils.log(Level.SEVERE, "Failed to save " + this.resource + " config file!");
            e.printStackTrace();
        }
    }
    
}
