package io.github.mooy1.infinitylib.core;

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
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A config which is able to save all of its comments and has some additional utility methods
 *
 * @author Mooy1
 */
public final class AddonConfig extends YamlConfiguration {

    private final YamlConfiguration defaults = new YamlConfiguration();
    private final Map<String, String> comments = new HashMap<>();
    private final File file;

    public AddonConfig(String path) {
        AbstractAddon addon = AbstractAddon.instance();
        file = new File(addon.getDataFolder(), path);
        super.defaults = defaults;
        loadDefaults(addon, path);
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

    /**
     * Removes unused/old keys from the users config
     */
    public void removeUnusedKeys() {
        for (String key : getKeys(true)) {
            if (!defaults.contains(key)) {
                set(key, null);
            }
        }
    }

    public void save() {
        try {
            save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(@Nonnull File file) throws IOException {
        if (AbstractAddon.environment() == Environment.LIVE) {
            super.save(file);
        }
    }

    public void reload() {
        if (file.exists()) {
            try {
                load(file);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        save();
    }

    @Nonnull
    @Override
    public YamlConfiguration getDefaults() {
        return defaults;
    }

    @Nullable
    String getComment(String key) {
        return comments.get(key);
    }

    @Nonnull
    @Override
    protected String buildHeader() {
        return "";
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

        }
        catch (Exception e) {
            e.printStackTrace();
            return defaultSave;
        }
    }

    private void loadDefaults(AbstractAddon addon, String name) {
        InputStream stream = addon.getResource(name);

        if (stream == null) {
            throw new IllegalStateException("No default config for " + name + "!");
        }
        else {
            try {
                String def = readDefaults(stream);
                defaults.loadFromString(def);
            }
            catch (Throwable e) {
                e.printStackTrace();
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
            }
            else {
                continue;
            }

            if (commentBuilder.length() != 1) {
                // Add the comment to the path and clear
                comments.put(pathBuilder.build(), commentBuilder.toString());
                commentBuilder = new StringBuilder("\n");
            }
            else if (pathBuilder.inMainSection()) {
                // The main section should always have spaces between keys
                comments.put(pathBuilder.build(), "\n");
            }
        }

        input.close();

        return yamlBuilder.toString();
    }

}
