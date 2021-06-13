package io.github.mooy1.infinitylib.items;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

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
        stone.setItemMeta(stone.getItemMeta());
        String saltID = SlimefunItems.SALT.getItemId();

        Assertions.assertNull(StackUtils.getID(stone));
        Assertions.assertEquals(saltID, StackUtils.getID(salt));
        Assertions.assertEquals(Material.STONE.name(), StackUtils.getIDorType(stone));
        Assertions.assertEquals(saltID, StackUtils.getIDorType(salt));
    }

    @Test
    void testGetItem() {
        ItemStack salt = SlimefunItems.SALT.clone();
        String saltID = SlimefunItems.SALT.getItemId();
        String stoneID = Material.STONE.name();

        SlimefunItemSetup.setup(MockBukkit.load(SlimefunPlugin.class));

        Assertions.assertNull(StackUtils.getItemByID(stoneID));
        Assertions.assertEquals(salt, StackUtils.getItemByID(saltID));
        Assertions.assertEquals(salt, StackUtils.getItemByIDorType(saltID));
        Assertions.assertEquals(new ItemStack(Material.STONE), StackUtils.getItemByIDorType(stoneID));
    }

    @Test
    void testGetDisplayName() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemMeta meta = salt.getItemMeta();

        Assertions.assertNotNull(meta);
        Assertions.assertEquals(SlimefunItems.SALT.getDisplayName(), StackUtils.getDisplayName(salt, meta));
        Assertions.assertEquals("TESTING", StackUtils.getDisplayName(salt));
        Assertions.assertEquals("TESTING", StackUtils.getDisplayName(new ItemStack(Material.STONE)));
    }

    @Test
    void testAddLore() {
        ItemStack item = new ItemStack(Material.STONE);
        item.setItemMeta(item.getItemMeta());
        StackUtils.addLore(item, "test");
        ItemMeta meta = item.getItemMeta();

        Assertions.assertNotNull(meta);
        Assertions.assertNotNull(meta.getLore());
        Assertions.assertEquals("test", meta.getLore().get(0));
    }

    @Test
    void testGetLiveMeta() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemMeta meta = StackUtils.getLiveMeta(salt);

        Assertions.assertNotNull(meta);
        Assertions.assertNull(StackUtils.getLiveMeta(new ItemStack(Material.STONE)));

        meta.setDisplayName("test");
        meta.setCustomModelData(1111);
        meta.setLore(Arrays.asList("lore", "lore"));
        ItemMeta check = salt.getItemMeta();

        Assertions.assertNotNull(check);
        Assertions.assertEquals("test", check.getDisplayName());
        Assertions.assertEquals(1111, check.getCustomModelData());
        Assertions.assertEquals(Arrays.asList("lore", "lore"), check.getLore());
    }

    @Test
    void testGetLivePDC() {
        ItemStack sf = new CustomItem(SlimefunItems.ELECTRO_MAGNET);
        ItemMeta meta = sf.getItemMeta();

        Assertions.assertNotNull(meta);
        Assertions.assertEquals(meta.getPersistentDataContainer(), StackUtils.getLivePDC(sf));
        Assertions.assertThrows(NullPointerException.class, () -> StackUtils.getLivePDC(new ItemStack(Material.STONE)));
    }

}
