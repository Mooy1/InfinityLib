package io.github.mooy1.infinitylib.persistence;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.persistence.PersistentDataContainerMock;
import io.github.mooy1.infinitylib.mocks.MockAddon;
import io.github.mooy1.infinitylib.mocks.MockUtils;

class TestPersistenceUtils {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testPersistentLocation() {
        NamespacedKey key = new NamespacedKey(MockUtils.mock(MockAddon.class), "key");
        PersistentDataContainer container = new PersistentDataContainerMock();

        WorldMock world = new WorldMock();
        server.addWorld(world);

        Location first = new Location(null, 1, 2, 3);
        Location second = new Location(world, 1, 2, 3);
        container.set(key, PersistenceUtils.LOCATION, first);
        Location third = container.get(key, PersistenceUtils.LOCATION);
        container.set(key, PersistenceUtils.LOCATION, second);
        Location fourth = container.get(key, PersistenceUtils.LOCATION);

        Assertions.assertEquals(first, third);
        Assertions.assertEquals(second, fourth);
    }

}
