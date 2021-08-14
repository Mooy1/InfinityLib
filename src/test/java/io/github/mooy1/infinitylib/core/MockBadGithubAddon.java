package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

public class MockBadGithubAddon extends MockAddon {

    protected MockBadGithubAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "#&", "fail", "\n", "auto-update");
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

}
