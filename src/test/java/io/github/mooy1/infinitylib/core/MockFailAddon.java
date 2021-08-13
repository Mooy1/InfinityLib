package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

class MockFailAddon extends MockAddon {

    protected MockFailAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "#&", "fail", "\n", null);
    }

}
