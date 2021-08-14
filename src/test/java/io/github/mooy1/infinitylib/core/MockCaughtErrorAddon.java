package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

public class MockCaughtErrorAddon extends MockAddon {

    protected MockCaughtErrorAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void load() {
        throw new Error() {
            @Override
            public void printStackTrace() {
                // It's meant to fail, no need to show stacktrace
            }
        };
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

    @Override
    protected void disable() {
        throw new Error() {
            @Override
            public void printStackTrace() {
                // It's meant to fail, no need to show stacktrace
            }
        };
    }

}
