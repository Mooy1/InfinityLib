package io.github.mooy1.infinitylib.player;

import io.github.mooy1.infinitylib.PluginUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class LeaveListener implements Listener {
    
    static {
        new LeaveListener();
    }
    
    private static final List<Map<UUID, ?>> maps = new ArrayList<>();
    
    private LeaveListener() {
        PluginUtils.registerEvents(this);
    }
    
    public static void add(Map<UUID, ?> map) {
        maps.add(map);
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        for (Map<UUID, ?> map : maps) {
            if (map != null) {
                map.remove(e.getPlayer().getUniqueId());
            } else {
                PluginUtils.runSync(() -> maps.remove(null));
            }
        }
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        for (Map<UUID, ?> map : maps) {
            if (map != null) {
                map.remove(e.getPlayer().getUniqueId());
            } else {
                PluginUtils.runSync(() -> maps.remove(null));
            }
        }
    }
    
}
