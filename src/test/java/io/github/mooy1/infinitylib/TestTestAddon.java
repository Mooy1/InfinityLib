package io.github.mooy1.infinitylib;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class TestTestAddon {

    private static ServerMock server;
    private static TestAddon addon;
    private static SlimefunPlugin slimefun;
    
    @BeforeAll
    public static void before() {
        server = MockBukkit.mock();
        slimefun = MockBukkit.load(SlimefunPlugin.class);
        addon = MockBukkit.load(TestAddon.class);
    }
    
    @Test
    public void test() {
        
    }

    @AfterAll
    public static void after() {
        MockBukkit.unmock();
    }
    
}
