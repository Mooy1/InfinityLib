package io.github.mooy1.infinitylib.tests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.recipes.RecipeMap;
import io.github.mooy1.infinitylib.recipes.ShapedRecipe;
import io.github.mooy1.infinitylib.recipes.ShapelessRecipe;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class TestRecipes {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testShapedRecipes() {
        ItemStack out = new ItemStack(Material.DIAMOND);
        ItemStack[] first = new ItemStack[] {
                new ItemStack(Material.STONE), SlimefunItems.SILVER_INGOT
        };
        ItemStack[] second = new ItemStack[] {
                new ItemStack(Material.STONE), null
        };
        ItemStack[] third = new ItemStack[] {
                new ItemStack(Material.STONE, 2), SlimefunItems.SILVER_INGOT
        };

        RecipeMap<ShapedRecipe> map = new RecipeMap<>(ShapedRecipe::new);
        map.add(first, out);
        ShapedRecipe output = map.get(third);
        output.consumeInput();

        Assertions.assertSame(map.get(first).getOutput(), out);
        Assertions.assertNull(map.get(second));
        Assertions.assertSame(output.getOutput(), out);
        Assertions.assertEquals(1, third[0].getAmount());
        Assertions.assertEquals(0, third[1].getAmount());
    }

    @Test
    void testShapelessRecipes() {
        ItemStack out = new ItemStack(Material.DIAMOND);
        ItemStack[] first = new ItemStack[] {
                new ItemStack(Material.STONE), SlimefunItems.SILVER_INGOT
        };
        ItemStack[] second = new ItemStack[] {
                new ItemStack(Material.STONE), null
        };
        ItemStack[] third = new ItemStack[] {
                new ItemStack(Material.STONE, 2), SlimefunItems.SILVER_INGOT.clone()
        };
        ItemStack[] fourth = new ItemStack[] {
                new ItemStack(Material.STONE), new ItemStack(Material.STONE), SlimefunItems.SILVER_INGOT
        };

        RecipeMap<ShapelessRecipe> map = new RecipeMap<>(ShapelessRecipe::new);
        map.add(first, out);
        ShapelessRecipe output = map.get(third);
        output.consumeInput();

        Assertions.assertSame(map.get(first).getOutput(), out);
        Assertions.assertSame(map.get(fourth).getOutput(), out);
        Assertions.assertNull(map.get(second));
        Assertions.assertSame(output.getOutput(), out);
        Assertions.assertEquals(1, third[0].getAmount());
        Assertions.assertEquals(0, third[1].getAmount());
    }

}
