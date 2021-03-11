package io.github.mooy1.infinitylib.players;

import io.github.mooy1.infinitylib.core.PluginUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A class which listens to players leaving and removes their uuid from maps.
 * 
 * @author Mooy1
 */
public final class LeaveListener implements Listener {
    
    private static final LeaveListener instance = new LeaveListener();
    
    private final List<Map<UUID, ?>> maps = new ArrayList<>();
    
    private LeaveListener() {
        PluginUtils.registerListener(this);
    }
    
    public static void add(@Nonnull Map<UUID, ?> map) {
        instance.maps.add(map);
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        for (Map<UUID, ?> map : this.maps) {
            map.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        for (Map<UUID, ?> map : this.maps) {
            map.remove(e.getPlayer().getUniqueId());
        }
    }
    
}
