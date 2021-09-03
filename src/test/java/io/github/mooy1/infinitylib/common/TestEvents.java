package io.github.mooy1.infinitylib.common;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;

class TestEvents implements Listener {

    private static boolean listenerCalled;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testCallEvent() {
        Events.call(new MockEvent());
        MockBukkit.getMock().getPluginManager().assertEventFired(MockEvent.class);
    }

    @Test
    void testRegisterListener() {
        Events.registerListener(this);
        Events.call(new MockEvent());
        Assertions.assertTrue(listenerCalled);
    }

    @Test
    void testAddHandler() {
        AtomicBoolean called = new AtomicBoolean();
        Events.addHandler(MockEvent.class, EventPriority.MONITOR, true, e -> called.set(true));
        Events.call(new MockEvent());
        Assertions.assertTrue(called.get());
    }

    @EventHandler
    void onEvent(MockEvent e) {
        listenerCalled = true;
    }

}
