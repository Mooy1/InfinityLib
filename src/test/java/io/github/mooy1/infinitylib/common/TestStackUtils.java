package io.github.mooy1.infinitylib.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;

public final class TestStackUtils {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        SlimefunItemSetup.setup(MockBukkit.load(Slimefun.class));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testGetId() {
        CustomItemDataService dataService = Slimefun.getItemDataService();
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        ItemStack item2 = new ItemStack(Material.IRON_BLOCK);

        Assertions.assertEquals(StackUtils.getId(item1), dataService.getItemData(item1).orElse(null));
        Assertions.assertEquals(StackUtils.getId(item2), dataService.getItemData(item2).orElse(null));
        Assertions.assertEquals(StackUtils.getId(item1.getItemMeta()), dataService.getItemData(item1.getItemMeta()).orElse(null));
        Assertions.assertEquals(StackUtils.getId(item2.getItemMeta()), dataService.getItemData(item2.getItemMeta()).orElse(null));
    }

    @Test
    void testGetIdOrType() {
        CustomItemDataService dataService = Slimefun.getItemDataService();
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        ItemStack item2 = new ItemStack(Material.IRON_BLOCK);

        Assertions.assertEquals(StackUtils.getIdOrType(item1), dataService.getItemData(item1).orElse(item1.getType().name()));
        Assertions.assertEquals(StackUtils.getIdOrType(item2), dataService.getItemData(item2).orElse(item2.getType().name()));
    }

    @Test
    void testItemById() {
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        String id1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD.getItemId();
        String id2 = Material.IRON_BLOCK.name();

        Assertions.assertEquals(StackUtils.itemById(id1), item1);
        Assertions.assertNull(StackUtils.itemById(id2));
    }

    @Test
    void testItemByIdOrType() {
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        ItemStack item2 = new ItemStack(Material.IRON_BLOCK);
        String id1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD.getItemId();
        String id2 = item2.getType().name();

        Assertions.assertEquals(StackUtils.itemByIdOrType(id1), item1);
        Assertions.assertEquals(StackUtils.itemByIdOrType(id2), item2);
    }

}
