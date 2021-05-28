package io.github.mooy1.infinitylib.tests;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.persistence.PersistentDataContainerMock;
import io.github.mooy1.infinitylib.persistence.PersistenceUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class TestPersistence {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testPersistentLocation() {
        MockBukkit.load(SlimefunPlugin.class);

        NamespacedKey key = new NamespacedKey(MockBukkit.load(TestAddon.class), "key");
        PersistentDataContainer container = new PersistentDataContainerMock();

        WorldMock world = new WorldMock();
        MockBukkit.getMock().addWorld(world);

        Location first = new Location(null, 1, 2, 3);
        Location second = new Location(world, 1, 2, 3);

        container.set(key, PersistenceUtils.LOCATION, first);

        Location third = container.get(key, PersistenceUtils.LOCATION);

        Assertions.assertEquals(third, first);

        container.set(key, PersistenceUtils.LOCATION, second);

        Location fourth = container.get(key, PersistenceUtils.LOCATION);

        Assertions.assertEquals(fourth, second);
    }

}
