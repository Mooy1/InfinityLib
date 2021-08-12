package io.github.mooy1.infinitylib.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.utils.MenuItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;

class TestStackUtils {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
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

        Assertions.assertNull(MenuItem.getID(stone));
        Assertions.assertEquals(saltID, MenuItem.getID(salt));
        Assertions.assertEquals(Material.STONE.name(), MenuItem.getIDorType(stone));
        Assertions.assertEquals(saltID, MenuItem.getIDorType(salt));
    }

    @Test
    void testGetItem() {
        ItemStack salt = SlimefunItems.SALT.clone();
        String saltID = SlimefunItems.SALT.getItemId();
        String stoneID = Material.STONE.name();

        SlimefunItemSetup.setup(MockBukkit.load(Slimefun.class));

        Assertions.assertNull(MenuItem.getItemByID(stoneID));
        Assertions.assertEquals(salt, MenuItem.getItemByID(saltID));
        Assertions.assertEquals(salt, MenuItem.getItemByIDorType(saltID));
        Assertions.assertEquals(new ItemStack(Material.STONE), MenuItem.getItemByIDorType(stoneID));
    }

    @Test
    void testGetDisplayName() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemMeta meta = salt.getItemMeta();

        Assertions.assertNotNull(meta);
        Assertions.assertEquals(SlimefunItems.SALT.getDisplayName(), MenuItem.getName(salt, meta));
        Assertions.assertEquals("TESTING", MenuItem.getName(salt));
        Assertions.assertEquals("TESTING", MenuItem.getName(new ItemStack(Material.STONE)));
    }

    @Test
    void testAddLore() {
        ItemStack item = new ItemStack(Material.STONE);
        MenuItem.addLore(item, "test");
        ItemMeta meta = item.getItemMeta();

        Assertions.assertNotNull(meta);
        Assertions.assertNotNull(meta.getLore());
        Assertions.assertEquals("test", meta.getLore().get(0));
    }

}
