package io.github.mooy1.infinitylib.configuration;

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
import javax.annotation.Nonnull;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.mooy1.infinitylib.AbstractAddon;

/**
 * A config which is able to save all of it's comments and has some additional utility methods
 * 
 * @author Mooy1
 */
public final class AddonConfig extends YamlConfiguration {

    private final Map<String, String> comments = new HashMap<>();
    private final AbstractAddon addon;
    private final File file;
    
    public AddonConfig(AbstractAddon addon, String name) {
        this.file = new File(addon.getDataFolder(), name);
        this.addon = addon;
        loadDefaults();
        reload();
    }

    public void addComment(String path, String comment) {
        this.comments.putIfAbsent(path, comment);
    }
    
    public int getInt(String path, int min, int max) {
        int val = getInt(path);
        if (val < min || val > max) {
            set(path, val = getDefaults().getInt(path));
        }
        return val;
    }

    public double getDouble(String path, double min, double max) {
        double val = getDouble(path);
        if (val < min || val > max) {
            set(path, val = getDefaults().getDouble(path));
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

    public void reload() {
        if (this.file.exists()) {
            try {
                load(this.file);
            } catch (InvalidConfigurationException e) {
                this.addon.log(Level.SEVERE, "There was an error loading the config '" + this.file.getPath() + "', resetting to default!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void loadFromString(@Nonnull String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);
        for (String key : getKeys(true)) {
            if (!this.defaults.contains(key)) {
                set(key, null);
            }
        }
        save();
    }
    
    @Nonnull
    @Override
    public String saveToString() {
        options().copyDefaults(true).copyHeader(false);
        String string = super.saveToString();
        StringBuilder save = new StringBuilder();
        String[] lines = string.split("\n");
        StringBuilder pathBuilder = new StringBuilder();
        List<Integer> dotIndexes = new ArrayList<>(2);
        for (String line : lines) {
            // append the comment and line
            String comment = this.comments.get(appendPath(pathBuilder, dotIndexes, line));
            if (comment == null) {
                save.append('\n').append(line).append('\n');
            } else {
                save.append(comment).append(line).append('\n');
            }
        }
        return save.toString();
    }

    private void loadDefaults() {
        BufferedReader input = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(this.addon.getResource(this.file.getPath()),
                () -> "No default config for " + this.file.getPath() + "!"), Charsets.UTF_8));
        
        StringBuilder yamlBuilder = new StringBuilder();
        StringBuilder commentBuilder = new StringBuilder();
        StringBuilder pathBuilder = new StringBuilder();
        List<Integer> dotIndexes = new ArrayList<>(2);
        
        // Load comments
        try {
            String line;
            while ((line = input.readLine()) != null) {
                // fix messed up lines
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
                    // no comment
                    appendPath(pathBuilder, dotIndexes, line);
                } else {
                    // add comment and reset
                    this.comments.put(appendPath(pathBuilder, dotIndexes, line), commentBuilder.toString());
                    commentBuilder = new StringBuilder();
                }
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        YamlConfiguration defaults = new YamlConfiguration();
        
        // load values
        try {
            defaults.loadFromString(yamlBuilder.toString());
        } catch (InvalidConfigurationException e) {
            this.addon.log(Level.SEVERE, "There was an error loading the default config of '" + this.file.getPath() + "', report this to the developers!");
            return;
        }
        
        // Auto Update
        if (this.file.getPath().equals("config.yml")) {
            String path = this.addon.getAutoUpdatePath();
            if (path != null) {
                defaults.set(path, true);
                addComment(path, "\n# This must be enabled to receive support!\n");
            }
        }
        
        setDefaults(defaults);
    }

    private static String appendPath(StringBuilder path, List<Integer> dotIndexes, String line) {
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
            path = new StringBuilder(key);
            if (dotIndexes.size() != 0) {
                dotIndexes.clear();
            }
        } else if (indent <= dotIndexes.size()) {
            path.replace(dotIndexes.get(indent - 1), path.length(), key);
        } else {
            path.append('.');
            dotIndexes.add(path.length());
            path.append(key);
        }
        return path.toString();
    }

}
