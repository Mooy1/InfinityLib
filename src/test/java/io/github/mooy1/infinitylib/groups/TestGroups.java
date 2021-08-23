package io.github.mooy1.infinitylib.groups;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.mooy1.infinitylib.common.Events;
import io.github.mooy1.infinitylib.core.MockAddon;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuListener;

class TestGroups {

    private static ServerMock server;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testGroups() throws ExecutionException, InterruptedException {
        SubGroup sub = new SubGroup("sub", new ItemStack(Material.STONE));
        MultiGroup multi = new MultiGroup("multi", new ItemStack(Material.DIAMOND), sub);
        PlayerMock p = server.addPlayer();
        CompletableFuture<PlayerProfile> profile = new CompletableFuture<>();
        PlayerProfile.get(p, profile::complete);

        new MenuListener(Objects.requireNonNull(Slimefun.instance()));
        multi.register(addon);
        multi.open(p, profile.get(), SlimefunGuideMode.SURVIVAL_MODE);

        Assertions.assertTrue(sub.isRegistered());
        Assertions.assertTrue(sub.isHidden(p));
        Assertions.assertFalse(multi.isHidden(p));
        Assertions.assertEquals(sub.getItem(p), p.getOpenInventory().getItem(9));

        Events.call(new InventoryClickEvent(p.getOpenInventory(), InventoryType.SlotType.CONTAINER,
                9, ClickType.LEFT, InventoryAction.PICKUP_ALL));

        Assertions.assertNull(p.getOpenInventory().getItem(9));

        Events.call(new InventoryClickEvent(p.getOpenInventory(), InventoryType.SlotType.CONTAINER,
                1, ClickType.LEFT, InventoryAction.PICKUP_ALL));

        Assertions.assertEquals(sub.getItem(p), p.getOpenInventory().getItem(9));
    }

}
