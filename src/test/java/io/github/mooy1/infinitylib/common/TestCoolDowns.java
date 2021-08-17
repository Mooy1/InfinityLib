package io.github.mooy1.infinitylib.common;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestCoolDowns {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testCheckAndReset() {
        CoolDowns min = new CoolDowns(0);
        CoolDowns max = new CoolDowns(System.currentTimeMillis());
        UUID uuid = server.addPlayer().getUniqueId();

        Assertions.assertTrue(min.checkAndReset(uuid));
        Assertions.assertTrue(max.checkAndReset(uuid));
        Assertions.assertTrue(min.check(uuid));
        Assertions.assertFalse(max.check(uuid));
    }

}
