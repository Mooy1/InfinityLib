package io.github.mooy1.infinitylib.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

import static io.github.mooy1.infinitylib.common.StackUtils.getId;
import static io.github.mooy1.infinitylib.common.StackUtils.getIdOrType;
import static io.github.mooy1.infinitylib.common.StackUtils.isSimilar;
import static io.github.mooy1.infinitylib.common.StackUtils.itemById;
import static io.github.mooy1.infinitylib.common.StackUtils.itemByIdOrType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestStackUtils {

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

        assertEquals(getId(item1), dataService.getItemData(item1).orElse(null));
        assertEquals(getId(item2), dataService.getItemData(item2).orElse(null));
        assertEquals(getId(item1.getItemMeta()), dataService.getItemData(item1.getItemMeta()).orElse(null));
        assertEquals(getId(item2.getItemMeta()), dataService.getItemData(item2.getItemMeta()).orElse(null));
    }

    @Test
    void testGetIdOrType() {
        CustomItemDataService dataService = Slimefun.getItemDataService();
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        ItemStack item2 = new ItemStack(Material.IRON_BLOCK);

        assertEquals(getIdOrType(item1), dataService.getItemData(item1).orElse(item1.getType().name()));
        assertEquals(getIdOrType(item2), dataService.getItemData(item2).orElse(item2.getType().name()));
    }

    @Test
    void testItemById() {
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        String id1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD.getItemId();
        String id2 = Material.IRON_BLOCK.name();

        assertEquals(itemById(id1), item1);
        assertNull(itemById(id2));
    }

    @Test
    void testItemByIdOrType() {
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        ItemStack item2 = new ItemStack(Material.IRON_BLOCK);
        String id1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD.getItemId();
        String id2 = item2.getType().name();

        assertEquals(itemByIdOrType(id1), item1);
        assertEquals(itemByIdOrType(id2), item2);
    }

    @Test
    void testIsSimilar() {
        ItemStack nul = null;
        ItemStack air = new ItemStack(Material.AIR);

        ItemStack stone = new ItemStack(Material.STONE);
        ItemStack salt = SlimefunItems.SALT;
        ItemStack sugar = new ItemStack(Material.SUGAR);
        ItemStack dust = new CustomItemStack(Material.SUGAR, "Dust");

        assertTrue(isSimilar(nul, nul));
        assertTrue(isSimilar(nul, air));
        assertTrue(isSimilar(air, air));

        assertFalse(isSimilar(nul, salt));
        assertFalse(isSimilar(air, salt));
        assertFalse(isSimilar(nul, sugar));
        assertFalse(isSimilar(air, sugar));
        assertFalse(isSimilar(nul, dust));
        assertFalse(isSimilar(air, dust));

        assertTrue(isSimilar(sugar, sugar));
        assertFalse(isSimilar(sugar, stone));
        assertFalse(isSimilar(sugar, salt));
        assertFalse(isSimilar(sugar, dust));

        assertTrue(isSimilar(salt, salt));
        assertFalse(isSimilar(salt, sugar));
        assertFalse(isSimilar(salt, dust));

        assertTrue(isSimilar(dust, dust));
    }

}
