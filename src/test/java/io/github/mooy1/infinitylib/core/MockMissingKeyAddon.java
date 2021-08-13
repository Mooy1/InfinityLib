package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

class MockMissingKeyAddon extends MockAddon {

    protected MockMissingKeyAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description(MockMissingKeyAddon.class), dataFolder, file,
                "Mooy1", "InfinityLib", "master", "fail");
    }

}
