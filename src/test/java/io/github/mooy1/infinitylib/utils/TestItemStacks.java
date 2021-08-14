package io.github.mooy1.infinitylib.utils;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;

class TestItemStacks {

    private static Slimefun slimefun;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        slimefun = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testGetID() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemStack stone = new ItemStack(Material.STONE);
        String saltID = SlimefunItems.SALT.getItemId();

        Assertions.assertNull(ItemStacks.getId(stone));
        Assertions.assertEquals(saltID, ItemStacks.getId(salt));
        Assertions.assertEquals(Material.STONE.name(), ItemStacks.getIdOrType(stone));
        Assertions.assertEquals(saltID, ItemStacks.getIdOrType(salt));
    }

    @Test
    void testGetItem() {
        ItemStack salt = SlimefunItems.SALT.clone();
        String saltID = SlimefunItems.SALT.getItemId();
        String stoneID = Material.STONE.name();

        SlimefunItemSetup.setup(slimefun);

        Assertions.assertNull(ItemStacks.fromId(stoneID));
        Assertions.assertEquals(salt, ItemStacks.fromId(saltID));
        Assertions.assertEquals(salt, ItemStacks.fromIdOrType(saltID));
        Assertions.assertEquals(new ItemStack(Material.STONE), ItemStacks.fromIdOrType(stoneID));
    }

    @Test
    void testGetDisplayName() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemMeta meta = Objects.requireNonNull(salt.getItemMeta());

        Assertions.assertEquals(SlimefunItems.SALT.getDisplayName(), ItemStacks.getName(salt, meta));
        Assertions.assertEquals("TESTING", ItemStacks.getName(salt));
        Assertions.assertEquals("TESTING", ItemStacks.getName(new ItemStack(Material.STONE)));
    }

    @Test
    void testAddLore() {
        ItemStack item = new ItemStack(Material.STONE);
        ItemStacks.addLore(item, "test");
        ItemMeta meta = item.getItemMeta();

        Assertions.assertNotNull(meta);
        Assertions.assertNotNull(meta.getLore());
        Assertions.assertEquals("test", meta.getLore().get(0));
    }

}
