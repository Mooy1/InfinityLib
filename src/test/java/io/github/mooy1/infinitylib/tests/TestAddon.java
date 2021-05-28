package io.github.mooy1.infinitylib.tests;

import java.io.File;

import javax.annotation.Nonnull;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class TestAddon extends AbstractAddon {

    public TestAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        MockBukkit.load(SlimefunPlugin.class);
    }

    @Nonnull
    @Override
    protected String getGithubPath() {
        return "Mooy1/TestAddon/master";
    }

}
