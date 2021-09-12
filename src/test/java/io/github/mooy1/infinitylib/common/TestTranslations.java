package io.github.mooy1.infinitylib.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestTranslations {

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
    void testGet() {
        assertEquals("test", Translations.get("test", "test"));
        assertNull(Translations.CONFIG.getString("test", null));
        assertEquals("test", Translations.DEFAULTS.getString("test", null));
        Translations.CONFIG.set("test", "new");
        assertEquals("new", Translations.get("test", "test"));
    }

}
