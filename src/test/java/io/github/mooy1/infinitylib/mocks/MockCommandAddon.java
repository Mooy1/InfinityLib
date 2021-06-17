package io.github.mooy1.infinitylib.mocks;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.commands.AbstractCommand;

public final class MockCommandAddon extends MockAddon {

    public MockCommandAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Nonnull
    @Override
    protected List<AbstractCommand> setupSubCommands() {
        return Collections.singletonList(new MockCommand());
    }

}
