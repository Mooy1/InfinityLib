package io.github.mooy1.infinitylib.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

class TestMachineBlock {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        Slimefun.getCfg().setValue("URID.enable-tickers", true);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testRegister() {
        MachineBlock block = new MachineBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
                new SlimefunItemStack("ID1", Material.STONE, "name"),
                RecipeType.ANCIENT_ALTAR, new ItemStack[0]);

        Assertions.assertThrows(IllegalStateException.class, () -> block.register(addon));

        block.ticksPerOutput(2);

        Assertions.assertThrows(IllegalStateException.class, () -> block.register(addon));

        block.energyPerTick(10);

        Assertions.assertDoesNotThrow(() -> block.register(addon));
        Assertions.assertEquals(20, block.energyCapacity);
        Assertions.assertSame(MachineLayout.DEFAULT, block.layout);
    }

    @Test
    void testProcess() {
        MachineBlock machine = new MachineBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
                new SlimefunItemStack("ID2", Material.STONE, "name"),
                RecipeType.ANCIENT_ALTAR, new ItemStack[0]);
        machine.ticksPerOutput(2).energyPerTick(10).register(addon);

        BlockMenuPreset preset = BlockMenuPreset.getPreset(machine.getId());
        Assertions.assertNotNull(preset);

        World world = server.addSimpleWorld("world");
        Location loc = new Location(world, 0, 0, 0);
        Block block = world.getBlockAt(loc);
        BlockMenu menu = new BlockMenu(preset, loc);
        ItemStack output = new CustomItemStack(SlimefunItems.SALT, 2);
        ItemStack input1 = SlimefunItems.COPPER_DUST; // TODO not consuming
        ItemStack input2 = new ItemStack(Material.NETHERITE_BLOCK, 2);

        machine.addRecipe(output, input1, input2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> machine.addRecipe(output));

        menu.replaceExistingItem(19, input1.clone());
        menu.replaceExistingItem(20, input2.clone());
        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertNotSame(output, menu.getItemInSlot(24));
        Assertions.assertEquals(output, menu.getItemInSlot(24));
        Assertions.assertNull(menu.getItemInSlot(19));
        Assertions.assertNull(menu.getItemInSlot(20));

        menu.replaceExistingItem(20, new CustomItemStack(input2, 4));
        menu.replaceExistingItem(21, new CustomItemStack(input1, 2));
        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertEquals(2, menu.getItemInSlot(24).getAmount());
        Assertions.assertEquals(1, menu.getItemInSlot(19).getAmount());
        Assertions.assertNull(menu.getItemInSlot(20));
        menu.replaceExistingItem(24, new CustomItemStack(output, 63));

        server.getScheduler().performOneTick();
        Assertions.assertFalse(machine.process(block, menu));
        server.getScheduler().performOneTick();
        Assertions.assertTrue(machine.process(block, menu));
        Assertions.assertEquals(64, menu.getItemInSlot(24).getAmount());
        Assertions.assertEquals(output, menu.getItemInSlot(25));
    }

}
