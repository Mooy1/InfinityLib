package io.github.mooy1.infinitylib.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.mooy1.infinitylib.AbstractAddon;

/**
 * A config which is able to save all of it's comments and has some additional utility methods
 *
 * @author Mooy1
 */
public final class AddonConfig extends YamlConfiguration {

    private final YamlConfiguration defaults = new YamlConfiguration();
    private final Map<String, String> comments = new HashMap<>();
    private final AbstractAddon addon;
    private final File file;

    public AddonConfig(@Nonnull AbstractAddon addon, @Nonnull String name) {
        this.file = new File(addon.getDataFolder(), name);
        super.defaults = this.defaults;
        this.addon = addon;
        loadDefaults(name);
    }

    public int getInt(@Nonnull String path, int min, int max) {
        int val = getInt(path);
        if (val < min || val > max) {
            set(path, val = getDefaults().getInt(path));
        }
        return val;
    }

    public double getDouble(@Nonnull String path, double min, double max) {
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

    @Override
    public void save(@Nonnull File file) throws IOException {
        if (this.addon.isNotTesting()) {
            super.save(file);
        }
    }

    public void reload() {
        if (this.file.exists()) try {
            load(this.file);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        save();
    }

    @Nonnull
    @Override
    public YamlConfiguration getDefaults() {
        return this.defaults;
    }

    @Nullable
    String getComment(String key) {
        return this.comments.get(key);
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
    }

    @Nonnull
    @Override
    public String saveToString() {
        options().copyDefaults(true).copyHeader(false).indent(2);
        String defaultSave = super.saveToString();

        try {
            String[] lines = defaultSave.split("\n");
            StringBuilder save = new StringBuilder();
            PathBuilder pathBuilder = new PathBuilder();

            for (String line : lines) {
                if (line.contains(":")) {
                    String comment = getComment(pathBuilder.append(line).build());
                    if (comment != null) {
                        save.append(comment);
                    }
                }
                save.append(line).append('\n');
            }
            return save.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return defaultSave;
        }
    }

    private void loadDefaults(String name) {
        InputStream stream = this.addon.getResource(name);

        if (stream == null) {
            throw new IllegalStateException("No default config for " + name + "!");
        } else try {
            String def = readDefaults(stream);
            this.defaults.loadFromString(def);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // Add an auto update thing just in case
        if (name.equals("config.yml")) {
            String path = this.addon.getAutoUpdatePath();
            if (path != null && !this.defaults.contains(path)) {
                this.defaults.set(path, true);
            }
        }

        reload();
    }

    private String readDefaults(@Nonnull InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder yamlBuilder = new StringBuilder();
        StringBuilder commentBuilder = new StringBuilder("\n");
        PathBuilder pathBuilder = new PathBuilder();
        String line;

        while ((line = input.readLine()) != null) {
            yamlBuilder.append(line).append('\n');

            if (StringUtils.isBlank(line)) {
                // Skip
                continue;
            }

            if (line.contains("#")) {
                // Add to comment of next path
                commentBuilder.append(line).append('\n');
                continue;
            }

            if (line.contains(":")) {
                // Its part of a path
                pathBuilder.append(line);
            } else {
                continue;
            }

            if (commentBuilder.length() != 1) {
                // Add the comment to the path and clear
                this.comments.put(pathBuilder.build(), commentBuilder.toString());
                commentBuilder = new StringBuilder("\n");
            } else if (pathBuilder.inMainSection()) {
                // The main section should always have spaces between keys
                this.comments.put(pathBuilder.build(), "\n");
            }
        }

        input.close();

        return yamlBuilder.toString();
    }

}
