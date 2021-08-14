package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

public class MockCallSuperAddon extends MockAddon {

    protected MockCallSuperAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void enable() {
        super.onEnable();
    }

    @Override
    protected void disable() {
        super.onDisable();
    }

}
