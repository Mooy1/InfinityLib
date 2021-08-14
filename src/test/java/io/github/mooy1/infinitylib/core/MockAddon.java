package io.github.mooy1.infinitylib.core;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class MockAddon extends AbstractAddon {

    protected MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file,
                        String githubUserName, String githubRepo, String autoUpdateBranch, String autoUpdateKey) {
        super(loader, description, dataFolder, file, githubUserName, githubRepo, autoUpdateBranch, autoUpdateKey);
        MockBukkit.load(Slimefun.class);
    }

    protected MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        this(loader, description, dataFolder, file,
                "Mooy1", "InfinityLib", "master", "auto-update");
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

}
