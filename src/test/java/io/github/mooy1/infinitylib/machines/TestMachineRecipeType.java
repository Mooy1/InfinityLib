package io.github.mooy1.infinitylib.machines;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public final class TestMachineRecipeType {

    private static MockAddon addon;
    private static MachineRecipeType type;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        type = new MachineRecipeType("key", null);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testKey() {
        Assertions.assertEquals(new RecipeType(new NamespacedKey(addon, "key"), null), type);
    }

    @Test
    void testSubscribe() {
        AtomicInteger recipesAccepted = new AtomicInteger();
        type.register(new ItemStack[0], null);
        type.sendRecipesTo((i, o) -> recipesAccepted.getAndIncrement());

        Assertions.assertEquals(1, recipesAccepted.get());

        type.register(new ItemStack[0], null);

        Assertions.assertEquals(2, recipesAccepted.get());
    }

}
