package io.github.mooy1.infinitylib.mocks;

import lombok.experimental.UtilityClass;

import org.bukkit.plugin.PluginDescriptionFile;

import be.seeseemelk.mockbukkit.MockBukkit;

@UtilityClass
public final class MockUtils {

   public static <T extends MockAddon> T mock(Class<T> clazz) {
       return MockBukkit.loadWith(clazz, new PluginDescriptionFile("MockAddon", "Mock", clazz.getName()));
   }

}
