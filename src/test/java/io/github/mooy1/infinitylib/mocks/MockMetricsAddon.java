package io.github.mooy1.infinitylib.mocks;

import java.io.File;

import javax.annotation.Nullable;

import lombok.Getter;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

public final class MockMetricsAddon extends MockAddon {

    @Getter
    private boolean metricsCreated = false;

    public MockMetricsAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Nullable
    @Override
    protected Metrics setupMetrics() {
        this.metricsCreated = true;
        return super.setupMetrics();
    }

}
