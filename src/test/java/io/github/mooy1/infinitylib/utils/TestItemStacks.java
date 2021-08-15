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
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class TestItemStacks {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
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
    void testGetDisplayName() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemMeta meta = Objects.requireNonNull(salt.getItemMeta());

        Assertions.assertEquals(SlimefunItems.SALT.getDisplayName(), ItemStacks.getName(salt, meta));
        Assertions.assertEquals("TESTING", ItemStacks.getName(salt));
        Assertions.assertEquals("TESTING", ItemStacks.getName(new ItemStack(Material.STONE)));
    }

}
