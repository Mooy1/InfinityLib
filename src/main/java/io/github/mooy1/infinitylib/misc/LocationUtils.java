package io.github.mooy1.infinitylib.misc;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collection of locations utils
 *
 * @author Mooy1
 */
@UtilityClass
public final class LocationUtils {
    
    @Nonnull
    public static Location[] getAdjacentLocations(@Nonnull Location l, boolean random) {
        List<Location> locations = new ArrayList<>(6);
        locations.add(l.clone().add(1, 0, 0));
        locations.add(l.clone().add(-1, 0, 0));
        locations.add(l.clone().add(0, 1, 0));
        locations.add(l.clone().add(0, -1, 0));
        locations.add(l.clone().add(0, 0, 1));
        locations.add(l.clone().add(0, 0, -1));

        if (random) {
            Collections.shuffle(locations);
        }

        return locations.toArray(new Location[6]);
    }
    
    public static void breakBlock(@Nonnull Block b, @Nonnull Player p) {
        PluginUtils.runSync(() -> {
            Bukkit.getPluginManager().callEvent(new BlockBreakEvent(b, p));
            b.setType(Material.AIR);
        }, 5L);
    }

}
