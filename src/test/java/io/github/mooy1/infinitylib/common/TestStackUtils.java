package io.github.mooy1.infinitylib.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public final class TestStackUtils {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testGetId() {
        CustomItemDataService dataService = Slimefun.getItemDataService();
        ItemStack item1 = SlimefunItems.ADVANCED_CIRCUIT_BOARD;
        ItemStack item2 = new ItemStack(Material.IRON_BLOCK);

        Assertions.assertEquals(StackUtils.getId(item1), dataService.getItemData(item1).orElse(null));
        Assertions.assertEquals(StackUtils.getId(item2), dataService.getItemData(item2).orElse(null));
        Assertions.assertEquals(StackUtils.getId(item1.getItemMeta()), dataService.getItemData(item1.getItemMeta()).orElse(null));
        Assertions.assertEquals(StackUtils.getId(item2.getItemMeta()), dataService.getItemData(item2.getItemMeta()).orElse(null));
    }

}
