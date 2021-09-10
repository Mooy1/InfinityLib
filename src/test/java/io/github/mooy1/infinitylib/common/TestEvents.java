package io.github.mooy1.infinitylib.common;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;

import static io.github.mooy1.infinitylib.common.Events.addHandler;
import static io.github.mooy1.infinitylib.common.Events.call;
import static io.github.mooy1.infinitylib.common.Events.registerListener;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        call(new MockEvent());
        MockBukkit.getMock().getPluginManager().assertEventFired(MockEvent.class);
    }

    @Test
    void testRegisterListener() {
        registerListener(this);
        call(new MockEvent());
        assertTrue(listenerCalled);
    }

    @Test
    void testAddHandler() {
        AtomicBoolean called = new AtomicBoolean();
        addHandler(MockEvent.class, EventPriority.MONITOR, true, e -> called.set(true));
        call(new MockEvent());
        assertTrue(called.get());
    }

    @EventHandler
    void onEvent(MockEvent e) {
        listenerCalled = true;
    }

}
