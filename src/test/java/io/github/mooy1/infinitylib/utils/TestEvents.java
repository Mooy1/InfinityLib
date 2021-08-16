package io.github.mooy1.infinitylib.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;

class TestEvents implements Listener {

    private static boolean eventFired;
    private static MockAddon addon;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testRegisterListener() {
        eventFired = false;
        Events.registerListener(this);
        Events.callEvent(new PluginEnableEvent(addon));
        Assertions.assertTrue(eventFired);
    }

    @Test
    void testAddHandler() {
        eventFired = false;
        Events.addHandler(PluginDisableEvent.class, EventPriority.MONITOR, true, e -> {
            eventFired = true;
        });
        Events.callEvent(new PluginDisableEvent(addon));
        Assertions.assertTrue(eventFired);
    }

    @EventHandler
    void onEvent(PluginEnableEvent e) {
        eventFired = true;
    }

}
