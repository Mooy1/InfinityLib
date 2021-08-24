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
import be.seeseemelk.mockbukkit.ServerMock;
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
class TestMachineBlock {

    private static ServerMock server;
    private static MockAddon addon;
    private static MachineBlock machine;
    private static Block block;
    private static ItemStack input1;
    private static ItemStack input2;
    private static ItemStack output;
    private static BlockMenu menu;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        Slimefun.getCfg().setValue("URID.enable-tickers", true);
        machine = new MachineBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
                new SlimefunItemStack("ID", Material.STONE, "name"),
                RecipeType.ANCIENT_ALTAR, new ItemStack[0]);
        block = server.addSimpleWorld("").getBlockAt(0, 0, 0);
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
        Assertions.assertThrows(IllegalStateException.class, () -> machine.register(addon));

        machine.ticksPerOutput(2);

        Assertions.assertThrows(IllegalStateException.class, () -> machine.register(addon));

        machine.energyPerTick(10);

        Assertions.assertDoesNotThrow(() -> machine.register(addon));
        Assertions.assertEquals(20, machine.energyCapacity);
        Assertions.assertSame(MachineLayout.DEFAULT, machine.layout);
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
        // TODO test subscribe
        machine.addRecipe(output, input1, input2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> machine.addRecipe(output));
    }

    @Test
    @Order(3)
    void testTicksPerOutput() {
        Assertions.assertFalse(machine.process(block, menu));
        server.getScheduler().performOneTick();
        Assertions.assertTrue(machine.process(block, menu));
        server.getScheduler().performOneTick();
        Assertions.assertFalse(machine.process(block, menu));
    }

    @Test
    void testProcess() {
        Assertions.assertFalse(machine.process(block, menu));

        menu.replaceExistingItem(19, input1.clone());
        menu.replaceExistingItem(20, input2.clone());
        menu.replaceExistingItem(24, null);
        menu.replaceExistingItem(24, null);

        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertNotSame(output, menu.getItemInSlot(24));
        Assertions.assertEquals(output, menu.getItemInSlot(24));
        Assertions.assertEquals(0, menu.getItemInSlot(19).getAmount());
        Assertions.assertEquals(0, menu.getItemInSlot(20).getAmount());
    }

    @Test
    void testShapelessProcessTwice() {
        menu.replaceExistingItem(19, new CustomItemStack(input2, 4));
        menu.replaceExistingItem(20, new CustomItemStack(input1, 2));
        menu.replaceExistingItem(24, null);
        menu.replaceExistingItem(25, null);

        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertEquals(2, menu.getItemInSlot(24).getAmount());
        Assertions.assertEquals(2, menu.getItemInSlot(19).getAmount());
        Assertions.assertEquals(1, menu.getItemInSlot(20).getAmount());

        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertEquals(4, menu.getItemInSlot(24).getAmount());
        Assertions.assertEquals(0, menu.getItemInSlot(19).getAmount());
        Assertions.assertEquals(0, menu.getItemInSlot(20).getAmount());
    }

    @Test
    void testSplitOutput() {
        menu.replaceExistingItem(19, new CustomItemStack(input2, 2));
        menu.replaceExistingItem(20, new CustomItemStack(input1, 1));
        menu.replaceExistingItem(24, new CustomItemStack(output, 63));
        menu.replaceExistingItem(25, null);

        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertEquals(64, menu.getItemInSlot(24).getAmount());
        Assertions.assertEquals(output, menu.getItemInSlot(25));
    }

    @Test
    void testPartialOutput() {
        menu.replaceExistingItem(19, new CustomItemStack(input2, 2));
        menu.replaceExistingItem(20, new CustomItemStack(input1, 1));
        menu.replaceExistingItem(24, new CustomItemStack(output, 64));
        menu.replaceExistingItem(25, new CustomItemStack(output, 63));

        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertEquals(64, menu.getItemInSlot(24).getAmount());
        Assertions.assertEquals(64, menu.getItemInSlot(25).getAmount());
        Assertions.assertEquals(0, menu.getItemInSlot(19).getAmount());
        Assertions.assertEquals(0, menu.getItemInSlot(20).getAmount());
    }

    @Test
    void testNoRoom() {
        menu.replaceExistingItem(19, new CustomItemStack(input2, 2));
        menu.replaceExistingItem(20, new CustomItemStack(input1, 1));
        menu.replaceExistingItem(24, new CustomItemStack(output, 64));
        menu.replaceExistingItem(25, new CustomItemStack(output, 64));

        Assertions.assertFalse(machine.process(block, menu));

        menu.replaceExistingItem(24, new CustomItemStack(input1, 1));
        menu.replaceExistingItem(25, new CustomItemStack(input2, 1));

        Assertions.assertFalse(machine.process(block, menu));
    }

}
