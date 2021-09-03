package io.github.mooy1.infinitylib.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.persistence.PersistentDataContainerMock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class TestPersistentType {

    private static ServerMock server;
    private static PersistentDataContainer container;
    private static NamespacedKey key;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        container = new PersistentDataContainerMock();
        key = new NamespacedKey(MockBukkit.createMockPlugin(), "key");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @Disabled(value = "MockBukkit issue")
    void testItemStack() {
        ItemStack item = new ItemStack(Material.OBSIDIAN);
        container.set(key, PersistentType.ITEM_STACK, item);
        Assertions.assertEquals(item, container.get(key, PersistentType.ITEM_STACK));

        item = SlimefunItems.ANCIENT_ALTAR;
        container.set(key, PersistentType.ITEM_STACK, item);
        Assertions.assertEquals(item, container.get(key, PersistentType.ITEM_STACK));
    }

    @Test
    @Disabled(value = "MockBukkit issue")
    void testItemStackOld() {
        ItemStack item = new ItemStack(Material.OBSIDIAN);
        container.set(key, PersistentType.ITEM_STACK_OLD, item);
        Assertions.assertEquals(item, container.get(key, PersistentType.ITEM_STACK_OLD));

        item = SlimefunItems.ANCIENT_ALTAR;
        container.set(key, PersistentType.ITEM_STACK_OLD, item);
        Assertions.assertEquals(item, container.get(key, PersistentType.ITEM_STACK_OLD));
    }

    @Test
    @Disabled(value = "MockBukkit issue")
    void testItemStackList() {
        List<ItemStack> list = new ArrayList<>();
        container.set(key, PersistentType.ITEM_STACK_LIST, list);
        Assertions.assertEquals(list, container.get(key, PersistentType.ITEM_STACK_LIST));

        list = Arrays.asList(new ItemStack(Material.PUMPKIN), SlimefunItems.ADVANCED_CIRCUIT_BOARD);
        container.set(key, PersistentType.ITEM_STACK_LIST, list);
        Assertions.assertEquals(list, container.get(key, PersistentType.ITEM_STACK_LIST));
    }

    @Test
    void testLocation() {
        WorldMock world = new WorldMock();
        server.addWorld(world);

        Location location = new Location(null, 1, 2, 3);
        container.set(key, PersistentType.LOCATION, location);
        Assertions.assertEquals(location, container.get(key, PersistentType.LOCATION));

        location = new Location(world, 1, 2, 3);
        container.set(key, PersistentType.LOCATION, location);
        Assertions.assertEquals(location, container.get(key, PersistentType.LOCATION));
    }

    @Test
    void testStringList() {
        List<String> list = new ArrayList<>();
        container.set(key, PersistentType.STRING_LIST, list);
        Assertions.assertEquals(list, container.get(key, PersistentType.STRING_LIST));

        list = Arrays.asList("a", "b", "c");
        container.set(key, PersistentType.STRING_LIST, list);
        Assertions.assertEquals(list, container.get(key, PersistentType.STRING_LIST));
    }

}
