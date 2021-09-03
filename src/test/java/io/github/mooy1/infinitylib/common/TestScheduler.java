package io.github.mooy1.infinitylib.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.scheduler.BukkitSchedulerMock;
import io.github.mooy1.infinitylib.core.MockAddon;

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

        Scheduler.run(() -> a.set(true));
        Scheduler.runAsync(() -> b.complete(true));

        scheduler.performOneTick();

        Assertions.assertTrue(a.get());
        Assertions.assertTrue(b.get());
    }

    @Test
    void testRunDelayed() throws ExecutionException, InterruptedException {
        AtomicBoolean a = new AtomicBoolean();
        CompletableFuture<Boolean> b = new CompletableFuture<>();

        Scheduler.run(2, () -> a.set(true));
        Scheduler.runAsync(2, () -> b.complete(true));

        scheduler.performOneTick();

        Assertions.assertFalse(a.get());
        Assertions.assertFalse(b.isDone());

        scheduler.performOneTick();

        Assertions.assertTrue(a.get());
        Assertions.assertTrue(b.get());
    }

    @Test
    void testRepeat() throws ExecutionException, InterruptedException {
        int times = 5;
        AtomicInteger a = new AtomicInteger();
        AtomicInteger b = new AtomicInteger();
        CompletableFuture<Boolean> c = new CompletableFuture<>();

        Scheduler.repeat(1, a::incrementAndGet);
        Scheduler.repeatAsync(1, () -> {
            if (b.incrementAndGet() == times) {
                c.complete(true);
            }
        });

        for (int i = 0; i < times; i++) {
            scheduler.performOneTick();
        }

        Assertions.assertEquals(times, a.get());
        Assertions.assertTrue(c.get());
    }

    @Test
    void testRepeatDelayed() throws ExecutionException, InterruptedException {
        int times = 4;
        AtomicInteger a = new AtomicInteger();
        AtomicInteger b = new AtomicInteger();
        CompletableFuture<Boolean> c = new CompletableFuture<>();

        Scheduler.repeat(1, 2, a::incrementAndGet);
        Scheduler.repeatAsync(1, 2, () -> {
            if (b.incrementAndGet() == times) {
                c.complete(true);
            }
        });

        for (int i = 0; i < times + 1; i++) {
            scheduler.performOneTick();
        }

        Assertions.assertEquals(times, a.get());
        Assertions.assertTrue(c.get());
    }

}
