package io.github.mooy1.infinitylib;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * A config which is able to save all of it's comments and has some additional utility methods
 * 
 * @author Mooy1
 */
public final class AddonConfig extends YamlConfiguration {

    private final Map<String, String> comments = new HashMap<>();
    private final AbstractAddon addon;
    private final File file;
    
    AddonConfig(AbstractAddon addon, String path) {
        this.addon = addon;
        this.file = new File(addon.getDataFolder(), path);
        
        try {
            YamlConfiguration defaults = new YamlConfiguration();
            defaults.loadFromString(loadDefaults(path));
            defaults.set("auto-update", true);
            this.comments.put("auto-update", "\n# This must be enabled to receive support!\n");

            for (String key : getKeys(true)) {
                if (!defaults.contains(key)) {
                    set(key, null);
                }
            }

            setDefaults(defaults);

            if (this.file.exists()) {
                load(this.file);
            } else {
                save(this.file);
            }
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getInt(String path, int min, int max) {
        int val = getInt(path);
        if (val < min || val > max) {
            set(path, val = getDefaults().getInt(path));
            this.addon.log(Level.WARNING, "Config value at " + path + " was out of bounds, resetting to default!");
        }
        return val;
    }

    public double getDouble(String path, double min, double max) {
        double val = getDouble(path);
        if (val < min || val > max) {
            set(path, val = getDefaults().getDouble(path));
            this.addon.log(Level.WARNING, "Config value at " + path + " was out of bounds, resetting to default!");
        }
        return val;
    }

    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean autoUpdatesEnabled() {
        return getBoolean("auto-update");
    }
    
    @Nonnull
    @Override
    public Configuration getDefaults() {
        return Objects.requireNonNull(super.getDefaults());
    }

    @Nonnull
    @Override
    protected String buildHeader() {
        return "";
    }
    
    @Nonnull
    @Override
    public String saveToString() {
        options().copyDefaults(true).copyHeader(false);
        String string = super.saveToString();
        StringBuilder save = new StringBuilder();
        String[] lines = string.split("\n");
        PathBuilder pathBuilder = new PathBuilder();
        for (String line : lines) {
            // append the comment and line
            save.append(this.comments.get(pathBuilder.append(line))).append(line).append('\n');
        }
        return save.toString();
    }

    private String loadDefaults(String filePath) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                this.addon.getResource(filePath), () -> "No default config for " + filePath + "!"), Charsets.UTF_8));
        StringBuilder yamlBuilder = new StringBuilder();
        PathBuilder pathBuilder = new PathBuilder();
        StringBuilder commentBuilder = new StringBuilder();
        try {
            String line;
            while ((line = input.readLine()) != null) {
                yamlBuilder.append(line).append('\n');
                // load comment
                if (StringUtils.isBlank(line)) {
                    // add blank line
                    commentBuilder.append(line).append('\n');
                } else if (line.contains("#")) {
                    // add new line if none before first comment
                    if (commentBuilder.length() == 0) {
                        commentBuilder.append('\n');
                    }
                    commentBuilder.append(line).append('\n');
                } else if (commentBuilder.length() == 0) {
                    // add new line
                    this.comments.put(pathBuilder.append(line), "\n");
                } else {
                    // add comment and reset
                    this.comments.put(pathBuilder.append(line), commentBuilder.toString());
                    commentBuilder = new StringBuilder();
                }
            }
        } finally {
            input.close();
        }
        return yamlBuilder.toString();
    }
    
    private static final class PathBuilder {

        private final List<Integer> dotIndexes = new ArrayList<>(2);
        private StringBuilder path;
        
        private String append(String line) {
            // count indent
            int indent = 0;
            for (int i = 0 ; i < line.length() ; i++) {
                if (line.charAt(i) == ' ') {
                    indent++;
                } else {
                    break;
                }
            }
            String key = line.substring(indent, line.lastIndexOf(':'));
            indent >>= 2;
            // change path
            if (indent == 0) {
                this.path = new StringBuilder(key);
                if (this.dotIndexes.size() != 0) {
                    this.dotIndexes.clear();
                }
            } else if (indent <= this.dotIndexes.size()) {
                this.path.replace(this.dotIndexes.get(indent - 1), this.path.length(), key);
            } else {
                this.path.append('.');
                this.dotIndexes.add(this.path.length());
                this.path.append(key);
            }
            return this.path.toString();
        }
        
    }

}
