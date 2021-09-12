package io.github.mooy1.infinitylib.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.scheduler.BukkitSchedulerMock;
import io.github.mooy1.infinitylib.core.MockAddon;

import static io.github.mooy1.infinitylib.common.Scheduler.repeat;
import static io.github.mooy1.infinitylib.common.Scheduler.repeatAsync;
import static io.github.mooy1.infinitylib.common.Scheduler.run;
import static io.github.mooy1.infinitylib.common.Scheduler.runAsync;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestScheduler {

    private static BukkitSchedulerMock scheduler;

    @BeforeAll
    public static void load() {
        scheduler = MockBukkit.mock().getScheduler();
        MockBukkit.load(MockAddon.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testRun() throws ExecutionException, InterruptedException {
        AtomicBoolean a = new AtomicBoolean();
        CompletableFuture<Boolean> b = new CompletableFuture<>();

        run(() -> a.set(true));
        runAsync(() -> b.complete(true));

        scheduler.performOneTick();

        assertTrue(a.get());
        assertTrue(b.get());
    }

    @Test
    void testRunDelayed() throws ExecutionException, InterruptedException {
        AtomicBoolean a = new AtomicBoolean();
        CompletableFuture<Boolean> b = new CompletableFuture<>();

        run(2, () -> a.set(true));
        runAsync(2, () -> b.complete(true));

        scheduler.performOneTick();

        assertFalse(a.get());
        assertFalse(b.isDone());

        scheduler.performOneTick();

        assertTrue(a.get());
        assertTrue(b.get());
    }

    @Test
    void testRepeat() throws ExecutionException, InterruptedException {
        int times = 5;
        AtomicInteger a = new AtomicInteger();
        AtomicInteger b = new AtomicInteger();
        CompletableFuture<Boolean> c = new CompletableFuture<>();

        repeat(1, a::incrementAndGet);
        repeatAsync(1, () -> {
            if (b.incrementAndGet() == times) {
                c.complete(true);
            }
        });

        for (int i = 0; i < times; i++) {
            scheduler.performOneTick();
        }

        assertEquals(times, a.get());
        assertTrue(c.get());
    }

    @Test
    void testRepeatDelayed() throws ExecutionException, InterruptedException {
        int times = 4;
        AtomicInteger a = new AtomicInteger();
        AtomicInteger b = new AtomicInteger();
        CompletableFuture<Boolean> c = new CompletableFuture<>();

        repeat(1, 2, a::incrementAndGet);
        repeatAsync(1, 2, () -> {
            if (b.incrementAndGet() == times) {
                c.complete(true);
            }
        });

        for (int i = 0; i < times + 1; i++) {
            scheduler.performOneTick();
        }

        assertEquals(times, a.get());
        assertTrue(c.get());
    }

}
