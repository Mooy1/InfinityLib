package io.github.mooy1.infinitylib.tests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.items.FastItemStack;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

class TestItems {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testStackUtils() {
        ItemStack salt = SlimefunItems.SALT.clone();
        ItemStack stone = new ItemStack(Material.STONE);
        String saltID = SlimefunItems.SALT.getItemId();
        String stoneID = Material.STONE.name();

        StackUtils.addLore(SlimefunItems.COBALT_PICKAXE, "test");
        SlimefunItemSetup.setup(MockBukkit.load(SlimefunPlugin.class));

        Assertions.assertNull(StackUtils.getID(stone));
        Assertions.assertEquals(saltID, StackUtils.getID(salt));
        Assertions.assertEquals(stoneID, StackUtils.getIDorType(stone));
        Assertions.assertEquals(saltID, StackUtils.getIDorType(salt));
        Assertions.assertNull(StackUtils.getItemByID(stoneID));
        Assertions.assertEquals(salt, StackUtils.getItemByID(saltID));
        Assertions.assertEquals(salt, StackUtils.getItemByIDorType(saltID));
        Assertions.assertEquals(stone, StackUtils.getItemByIDorType(stoneID));
        Assertions.assertEquals(SlimefunItems.SALT.getDisplayName(), StackUtils.getDisplayName(salt));
        Assertions.assertEquals("test", SlimefunItems.COBALT_PICKAXE.getItemMeta().getLore().get(0));
    }

    @Test
    void testFastItemStack() {
        ItemStack stone = new ItemStack(Material.STONE);
        SlimefunItemStack salt = SlimefunItems.SALT;
        ItemStack pick = SlimefunItems.COBALT_PICKAXE.clone();
        FastItemStack first = new FastItemStack(stone);
        FastItemStack second = new FastItemStack(salt);
        FastItemStack third = new FastItemStack(pick);

        third.setType(Material.DIAMOND);

        Assertions.assertNull(first.getID());
        Assertions.assertSame(pick.getType(), third.getType());
        Assertions.assertEquals(first.getType().name(), first.getIDorType());
        Assertions.assertEquals(salt.getItemId(), second.getID());
        Assertions.assertEquals(salt.getItemId(), second.getIDorType());
        Assertions.assertSame(first.getItemMeta(), first.getItemMeta());
        Assertions.assertEquals(pick.getEnchantments(), third.getEnchantments());
        Assertions.assertFalse(FastItemStack.of(first).getOriginal() instanceof FastItemStack);
        Assertions.assertFalse(first.fastEquals(second));
        Assertions.assertFalse(second.fastEquals(third));
        Assertions.assertTrue(third.fastEquals(SlimefunItems.COBALT_PICKAXE));
    }

}
