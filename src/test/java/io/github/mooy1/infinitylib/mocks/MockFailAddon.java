package io.github.mooy1.infinitylib.mocks;

import java.io.File;

import javax.annotation.Nonnull;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

public final class MockFailAddon extends MockAddon {

    public MockFailAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Nonnull
    @Override
    protected String getGithubPath() {
        return "FAIL";
    }

}
