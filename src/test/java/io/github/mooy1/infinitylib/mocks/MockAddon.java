package io.github.mooy1.infinitylib.mocks;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.mooy1.infinitylib.commands.AbstractCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public final class MockAddon extends AbstractAddon {

    public MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);

        MockBukkit.load(SlimefunPlugin.class);
    }

    @Override
    protected void enable() {
        throw new Error();
    }

    @Override
    protected void disable() {

    }

    @Nonnull
    @Override
    protected List<AbstractCommand> setupSubCommands() {
        return Collections.singletonList(new MockCommand());
    }

    @Nonnull
    @Override
    protected Metrics setupMetrics() {
        return new MockMetrics(this, -1);
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
