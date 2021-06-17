package io.github.mooy1.infinitylib.mocks;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.commands.AbstractCommand;

public final class MockErrorAddon extends MockAddon {

    public MockErrorAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void enable() {
        throw new Error();
    }

    @Nullable
    @Override
    protected Metrics setupMetrics() {
        throw new Error();
    }

    @Nullable
    @Override
    protected List<AbstractCommand> setupSubCommands() {
        throw new Error();
    }

}
