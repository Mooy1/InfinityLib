package io.github.mooy1.infinitylib.mocks;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class MockMetrics extends Metrics {

    public static boolean created;

    public MockMetrics(JavaPlugin plugin, int serviceId) {
        super(plugin, serviceId);
        created = true;
    }

}
