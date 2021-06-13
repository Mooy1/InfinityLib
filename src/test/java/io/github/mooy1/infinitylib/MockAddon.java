package io.github.mooy1.infinitylib;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.commands.AbstractCommand;
import io.github.mooy1.infinitylib.commands.MockCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public final class MockAddon extends AbstractAddon {

    @Getter
    private boolean metricsCreated;

    public MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);

        MockBukkit.load(SlimefunPlugin.class);
    }

    @Override
    protected void enable() {
        throw new Error() {
            @Override
            public void printStackTrace() {
                // Don't need to print anything since its a test
            }
        };
    }

    @Override
    protected void disable() {

    }

    @Nonnull
    @Override
    protected List<AbstractCommand> setupSubCommands() {
        return Collections.singletonList(new MockCommand());
    }

    @Nullable
    @Override
    protected Metrics setupMetrics() {
        this.metricsCreated = true;
        return null;
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
