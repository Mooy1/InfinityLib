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
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A config which is able to save all of its comments and has some additional utility methods
 *
 * @author Mooy1
 */
@ParametersAreNonnullByDefault
public final class AddonConfig extends YamlConfiguration {

    private final YamlConfiguration defaults = new YamlConfiguration();
    private final Map<String, String> comments = new HashMap<>();
    private final File file;

    /**
     * Loads a config from within the addons' data folder, and uses the addon's defaults
     */
    public AddonConfig(String path) {
        this(new File(AbstractAddon.instance().getDataFolder(), path));

        AbstractAddon instance = AbstractAddon.instance();
        try (InputStream stream = instance.getResource(path)) {
            if (stream != null) {
                String def = readDefaults(stream);
                defaults.loadFromString(def);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        save();
    }

    /**
     * Loads a config from the given file with no defaults
     */
    public AddonConfig(File file) {
        super.defaults = defaults;
        this.file = file;
        reload();
    }

    /**
     * Gets an integer within the given range, inclusive
     */
    public int getInt(String path, int min, int max) {
        int val = getInt(path);
        if (val < min) {
            set(path, val = min);
        }
        else if (val > max) {
            set(path, val = max);
        }
        return val;
    }

    /**
     * Gets a double within the given range, inclusive
     */
    public double getDouble(String path, double min, double max) {
        double val = getDouble(path);
        if (val < min) {
            set(path, val = min);
        }
        else if (val > max) {
            set(path, val = max);
        }
        return val;
    }

    /**
     * Removes keys from the config which are not present in the defaults. This will clean out old keys.
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

    private String readDefaults(@Nonnull InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder yamlBuilder = new StringBuilder();
        StringBuilder commentBuilder = new StringBuilder("\n");
        PathBuilder pathBuilder = new PathBuilder();
        String line;

        while ((line = input.readLine()) != null) {
            yamlBuilder.append(line).append('\n');

            if (StringUtils.isBlank(line)) {
                continue;
            }

            if (line.contains("#")) {
                commentBuilder.append(line).append('\n');
                continue;
            }

            if (line.contains(":")) {
                pathBuilder.append(line);

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
        }

        return yamlBuilder.toString();
    }

}
