package io.github.mooy1.infinitylib.mocks;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public class MockAddon extends AbstractAddon {

    public MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);

        MockBukkit.load(SlimefunPlugin.class);
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

    @Nullable
    @Override
    public String getAutoUpdatePath() {
        return null;
    }

    @Nonnull
    @Override
    protected String getGithubPath() {
        return "Mooy1/InfinityLib/master";
    }

}
