package io.github.mooy1.otheraddon;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.core.Environment;
import io.github.mooy1.infinitylib.core.MockAddon;

public final class MockOtherAddon extends MockAddon {

    public MockOtherAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file, Environment environment) {
        super(loader, description, dataFolder, file, environment, null);
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

}
