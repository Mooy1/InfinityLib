package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

class MockCaughtErrorAddon extends MockAddon {

    public MockCaughtErrorAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void enable() {
        throw new Error() {
            @Override
            public void printStackTrace() {
                // It's meant to fail, no need to show stacktrace
            }
        };
    }

}
