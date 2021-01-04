package io.github.mooy1.infinitylib.player;

import io.github.mooy1.infinitylib.PluginUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public final class LeaveListener implements Listener {
    
    private final Map<UUID, ?> map;
    
    public LeaveListener(@Nonnull Map<UUID, ?> map) {
        this.map = map;
        PluginUtils.registerEvents(this);
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        this.map.remove(e.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        this.map.remove(e.getPlayer().getUniqueId());
    }
    
}
