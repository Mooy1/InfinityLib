package io.github.mooy1.infinitylib.players;

import io.github.mooy1.infinitylib.AbstractAddon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class LeaveListener implements Listener {

    public static void create(AbstractAddon addon, Map<UUID, ?> map) {
        addon.registerListener(new LeaveListener(map));
    }
    
    private final Map<UUID, ?> map;
    
    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        this.map.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onKick(PlayerKickEvent e) {
        this.map.remove(e.getPlayer().getUniqueId());
    }
    
}
