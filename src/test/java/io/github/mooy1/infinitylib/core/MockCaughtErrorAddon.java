package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

class MockCaughtErrorAddon extends MockAddon {

    public MockCaughtErrorAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description(MockCaughtErrorAddon.class), dataFolder, file);
    }

    @Override
    protected void enable() {
        throw new Error();
    }

}
