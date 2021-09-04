package io.github.mooy1.infinitylib.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestCraftingBlock {

    private static MockAddon addon;
    private static CraftingBlock machine;
    private static Block block;
    private static ItemStack input1;
    private static ItemStack input2;
    private static ItemStack output;
    private static BlockMenu menu;

    @BeforeAll
    public static void load() {
        block = MockBukkit.mock().addSimpleWorld("").getBlockAt(0, 0, 0);
        addon = MockBukkit.load(MockAddon.class);
        Slimefun.getCfg().setValue("URID.enable-tickers", true);
        machine = new CraftingBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
                new SlimefunItemStack("ID", Material.STONE, "name"),
                RecipeType.ANCIENT_ALTAR, new ItemStack[0]);
        output = new CustomItemStack(SlimefunItems.SALT, 2);
        input1 = SlimefunItems.COPPER_DUST;
        input2 = new ItemStack(Material.NETHERITE_BLOCK, 2);
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
    void testBlockMenuPreset() {
        BlockMenuPreset preset = BlockMenuPreset.getPreset(machine.getId());
        Assertions.assertNotNull(preset);
        menu = new BlockMenu(preset, block.getLocation());
    }

    @Test
    @Order(2)
    void testAddRecipes() {
        machine.addRecipe(output, input1, input2, null, null, null, null, null, null, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> machine.addRecipe(output));
    }

    @Test
    void testProcess() {
        Assertions.assertNull(machine.getOutput(block, machine.getInput(menu)));

        menu.replaceExistingItem(10, input1.clone());
        menu.replaceExistingItem(11, input2.clone());
        menu.replaceExistingItem(25, null);
        ItemStack[] input = machine.getInput(menu);

        CraftingBlockRecipe recipe = machine.getOutput(block, input);

        Assertions.assertNotNull(recipe);

        recipe.consume(input);

        Assertions.assertEquals(0, menu.getItemInSlot(10).getAmount());
        Assertions.assertEquals(0, menu.getItemInSlot(11).getAmount());
    }

}
