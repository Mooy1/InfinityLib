package io.github.mooy1.infinitylib.players;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestCoolDownMap {

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
        CoolDownMap min = new CoolDownMap(0);
        CoolDownMap max = new CoolDownMap(System.currentTimeMillis());
        UUID uuid = server.addPlayer().getUniqueId();

        Assertions.assertTrue(min.checkAndReset(uuid));
        Assertions.assertTrue(max.checkAndReset(uuid));
        Assertions.assertTrue(min.check(uuid));
        Assertions.assertFalse(max.check(uuid));
    }

}
