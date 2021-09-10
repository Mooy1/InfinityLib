package io.github.mooy1.infinitylib.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestMachineBlock {

    private static MockAddon addon;
    private static MachineBlock machine;
    private static ItemStack input1;
    private static ItemStack input2;
    private static ItemStack output;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        Slimefun.getCfg().setValue("URID.enable-tickers", true);
        machine = new MachineBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
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
        assertThrows(IllegalStateException.class, () -> machine.register(addon));

        machine.ticksPerOutput(2);

        assertThrows(IllegalStateException.class, () -> machine.register(addon));

        machine.energyPerTick(10);

        assertDoesNotThrow(() -> machine.register(addon));
        assertEquals(20, machine.energyCapacity);
        assertSame(MachineLayout.MACHINE_DEFAULT, machine.layout);
    }

    @Test
    @Order(1)
    void testAddRecipes() {
        machine.addRecipe(output, input1, input2);
        assertThrows(IllegalArgumentException.class, () -> machine.addRecipe(output));
    }

    @Test
    void testProcess() {
        ItemStack[] input = new ItemStack[2];
        assertNull(machine.getOutput(input));

        input[0] = input1.clone();
        input[1] = input2.clone();
        MachineBlockRecipe out = machine.getOutput(input);

        assertNotNull(out);
        assertSame(output, out.output);

        out.consume();

        assertEquals(0, input[0].getAmount());
        assertEquals(0, input[1].getAmount());
        assertNull(machine.getOutput(input));

        input[0] = new CustomItemStack(input2, 4);
        input[1] = new CustomItemStack(input1, 2);

        out = machine.getOutput(input);

        assertNotNull(out);

        out.consume();

        assertEquals(2, input[0].getAmount());
        assertEquals(1, input[1].getAmount());
    }

}
