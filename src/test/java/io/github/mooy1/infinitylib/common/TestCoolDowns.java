package io.github.mooy1.infinitylib.common;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertTrue(min.checkAndReset(uuid));
        assertTrue(max.checkAndReset(uuid));
        assertTrue(min.check(uuid));
        assertFalse(max.check(uuid));
    }

}
