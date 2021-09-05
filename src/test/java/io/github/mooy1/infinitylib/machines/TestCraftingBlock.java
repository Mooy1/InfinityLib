package io.github.mooy1.infinitylib.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestCraftingBlock {

    private static MockAddon addon;
    private static CraftingBlock machine;
    private static ItemStack input1;
    private static ItemStack input2;
    private static ItemStack output;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        machine = new CraftingBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
                new SlimefunItemStack("ID", Material.STONE, "name"),
                RecipeType.ANCIENT_ALTAR, new ItemStack[0]);
        output = new CustomItemStack(SlimefunItems.SALT, 2);
        input1 = SlimefunItems.COPPER_DUST.clone();
        input2 = new ItemStack(Material.NETHERITE_BLOCK, 2).clone();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @Order(0)
    void testRegister() {
        machine.register(addon);
        Assertions.assertSame(MachineLayout.CRAFTING_DEFAULT, machine.layout);
    }

    @Test
    @Order(1)
    void testAddRecipes() {
        machine.addRecipe(output, input1, input2, null, null, null, null, null, null, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> machine.addRecipe(output));
    }

    @Test
    void testProcess() {
        ItemStack[] input = new ItemStack[9];
        input[0] = input1;
        Assertions.assertNull(machine.getOutput(input));

        input[1] = input2;
        CraftingBlockRecipe recipe = machine.getOutput(input);
        Assertions.assertNotNull(recipe);

        recipe.consume(input);
        Assertions.assertEquals(0, input[0].getAmount());
        Assertions.assertEquals(0, input[1].getAmount());
    }

}
