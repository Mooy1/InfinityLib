package io.github.mooy1.infinitylib.player;

import lombok.Setter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Collection of utils for sending messages to players and broadcasting
 *
 * @author Mooy1
 * 
 */
public final class MessageUtils {
    
    public MessageUtils() {
        new LeaveListener(coolDowns);
    }
    
    private static final Map<UUID, Long> coolDowns = new HashMap<>();
    @Setter
    public static String prefix = null;

    public static void message(@Nonnull Player p, @Nonnull String... messages) {
        validate();
        for (String m : messages) {
            p.sendMessage(prefix + ChatColors.color(m));
        }
    }

    public static void broadcast(@Nonnull String message) {
        validate();
        Bukkit.broadcastMessage(prefix + message);
    }

    public static void messageWithCD(@Nonnull Player p, long coolDown, @Nonnull String... messages) {
        if (coolDowns.containsKey(p.getUniqueId()) && System.currentTimeMillis() - coolDowns.get(p.getUniqueId()) < coolDown) {
            return;
        }
        coolDowns.put(p.getUniqueId(), System.currentTimeMillis());
        
        message(p, messages);
    }

    public static void messagePlayersInInv(@Nonnull BlockMenu inv, @Nonnull String... messages) {
        if (inv.hasViewer()) {
            for (HumanEntity viewer : inv.toInventory().getViewers().toArray(new HumanEntity[1])) {
                message((Player) viewer, messages);
            }
        }
    }

    private static void validate() {
        Validate.notNull(prefix, "Make sure to set the the prefix");
    }
    
}
