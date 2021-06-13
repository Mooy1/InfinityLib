package io.github.mooy1.infinitylib.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class TestRecipeMap {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testShapedRecipe() {
        ItemStack out = new ItemStack(Material.DIAMOND);
        ItemStack[] first = new ItemStack[] {
                new ItemStack(Material.STONE), SlimefunItems.SILVER_INGOT.clone()
        };
        ItemStack[] second = new ItemStack[] {
                new ItemStack(Material.STONE), null
        };
        ItemStack[] third = new ItemStack[] {
                new ItemStack(Material.STONE, 2), SlimefunItems.SILVER_INGOT.clone()
        };
        ItemStack[] fourth = new ItemStack[] {
                new ItemStack(Material.STONE, 2), SlimefunItems.SILVER_INGOT.clone()
        };

        RecipeMap<ItemStack> map = new RecipeMap<>(ShapedRecipe::new);
        map.put(first, out);
        RecipeOutput<ItemStack> output = map.get(fourth);

        Assertions.assertSame(out, map.getNoConsume(first));
        Assertions.assertNull(map.get(second));
        Assertions.assertSame(out, map.getAndConsume(third));
        Assertions.assertEquals(1, third[0].getAmount());
        Assertions.assertEquals(0, third[1].getAmount());
        Assertions.assertNotNull(output);
        Assertions.assertSame(out, output.getAndConsume());
        Assertions.assertArrayEquals(first, output.getRecipeInput());
        Assertions.assertArrayEquals(fourth, output.getOriginalInput());
        Assertions.assertEquals(1, fourth[0].getAmount());
        Assertions.assertEquals(0, fourth[1].getAmount());
    }

    @Test
    void testShapelessRecipe() {
        ItemStack out = new ItemStack(Material.DIAMOND);
        ItemStack[] first = new ItemStack[] {
                new ItemStack(Material.STONE), SlimefunItems.SILVER_INGOT.clone()
        };
        ItemStack[] second = new ItemStack[] {
                new ItemStack(Material.STONE), null
        };
        ItemStack[] third = new ItemStack[] {
                new ItemStack(Material.STONE, 2), SlimefunItems.SILVER_INGOT.clone()
        };
        ItemStack[] fourth = new ItemStack[] {
                new ItemStack(Material.STONE), new ItemStack(Material.STONE), SlimefunItems.SILVER_INGOT.clone()
        };

        RecipeMap<ItemStack> map = new RecipeMap<>(ShapelessRecipe::new);
        map.put(first, out);

        Assertions.assertSame(out, map.getNoConsume(first));
        Assertions.assertNull(map.get(second));
        Assertions.assertSame(out, map.getAndConsume(third));
        Assertions.assertSame(out, map.getNoConsume(fourth));
        Assertions.assertEquals(1, third[0].getAmount());
        Assertions.assertEquals(0, third[1].getAmount());
    }

}
