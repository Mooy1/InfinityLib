package io.github.mooy1.infinitylib.menus;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Collection of locations utils
 *
 * @author Mooy1
 */
@UtilityClass
public final class LocationUtils {

    /**
     * This method returns an array of all adjacent locations from a location
     *
     * @param l location
     *
     * @return adjacent locations
     */
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

    /**
     * This method gets an adjacent location from a location and the direction
     *
     * @param l         location
     * @param direction direction which can be found between 2 locations with getDirectionInt
     *
     * @return the location in the direction
     */
    @Nullable
    public static Location getRelativeLocation(@Nonnull Location l, int direction) {
        Location clone = l.clone();
        switch (direction) {
            case 0:
                return clone.add(1, 0, 0);
            case 1:
                return clone.add(-1, 0, 0);
            case 2:
                return clone.add(0, 1, 0);
            case 3:
                return clone.add(0, -1, 0);
            case 4:
                return clone.add(0, 0, 1);
            case 5:
                return clone.add(0, 0, -1);
            default:
                return null;
        }
    }

    /**
     * This method gets the direction number from 2 locations
     *
     * @param current the current location
     * @param target  the target location
     *
     * @return the direction int to go from the current to the target
     */
    public static int getDirectionInt(@Nonnull Location current, @Nonnull Location target) {
        int currentX = current.getBlockX();
        int currentY = current.getBlockY();
        int currentZ = current.getBlockZ();
        int targetX = target.getBlockX();
        int targetY = target.getBlockY();
        int targetZ = target.getBlockZ();

        if (targetX - currentX == 1) {
            return 0;
        } else if (targetX - currentX == -1) {
            return 1;
        } else if (targetY - currentY == 1) {
            return 2;
        } else if (targetY - currentY == -1) {
            return 3;
        } else if (targetZ - currentZ == 1) {
            return 4;
        } else if (targetZ - currentZ == -1) {
            return 5;
        } else {
            return -1;
        }
    }

    public static void breakBlock(@Nonnull Block b, @Nonnull Player p) {
        PluginUtils.runSync(() -> {
            Bukkit.getPluginManager().callEvent(new BlockBreakEvent(b, p));
            b.setType(Material.AIR);
        }, 5);
    }

    @Nullable
    public static String toString(@Nullable Location l) {
        if (l == null || l.getWorld() == null) {
            return null;
        }
        return l.getX() + " " + l.getY() + " " + l.getZ() + " " + l.getWorld().getName();
    }
    
    private static final Pattern pattern = Pattern.compile(" ");

    @Nullable
    public static Location fromString(@Nullable String location) {
        if (location == null) {
            return null;
        }
        String[] split = pattern.split(location);
        return new Location(Bukkit.getServer().getWorld(split[3]), Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }


}
