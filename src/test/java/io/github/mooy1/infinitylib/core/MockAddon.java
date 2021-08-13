package io.github.mooy1.infinitylib.core;

import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class MockAddon extends AbstractAddon {

    protected MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file, "Mooy1", "InfinityLib", "master", "");
        MockBukkit.load(Slimefun.class);
    }

    protected MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
                            String githubUserName, String githubRepo, String githubBranch, @Nullable String autoUpdateKey) {
        super(loader, description, dataFolder, file, githubUserName, githubRepo, githubBranch, autoUpdateKey);
        MockBukkit.load(Slimefun.class);
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

}
