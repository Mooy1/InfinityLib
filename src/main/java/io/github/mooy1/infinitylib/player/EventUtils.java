package io.github.mooy1.infinitylib.player;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;

public final class EventUtils {

    public static boolean checkRightClickEvent(PlayerRightClickEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND || (e.getClickedBlock().isPresent() &&
                !SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), e.getClickedBlock().get(), ProtectableAction.INTERACT_BLOCK))) {
            return false;
        }
        e.setUseBlock(Event.Result.DENY);
        return true;
    }
    
}
