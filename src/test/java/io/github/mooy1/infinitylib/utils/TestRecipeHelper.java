package io.github.mooy1.infinitylib.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class TestRecipeHelper {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testGetId() {
        ItemStack stone = new ItemStack(Material.STONE);
        ItemStack salt = SlimefunItems.SALT.clone();
        String saltID = SlimefunItems.SALT.getItemId();

        Assertions.assertNull(RecipeHelper.getId(stone));
        Assertions.assertNull(RecipeHelper.getId(stone.getItemMeta()));
        Assertions.assertEquals(saltID, RecipeHelper.getId(salt));
        Assertions.assertEquals(saltID, RecipeHelper.getId(salt.getItemMeta()));
    }

    @Test
    void testGetIdOrType() {
        ItemStack stone = new ItemStack(Material.STONE);
        ItemStack salt = SlimefunItems.SALT.clone();
        String saltID = SlimefunItems.SALT.getItemId();

        Assertions.assertEquals(Material.STONE.name(), RecipeHelper.getIdOrType(stone));
        Assertions.assertEquals(Material.STONE.name(), RecipeHelper.getIdOrType(stone, stone.getItemMeta()));
        Assertions.assertEquals(saltID, RecipeHelper.getIdOrType(salt));
        Assertions.assertEquals(saltID, RecipeHelper.getIdOrType(salt, salt.getItemMeta()));
    }

}
