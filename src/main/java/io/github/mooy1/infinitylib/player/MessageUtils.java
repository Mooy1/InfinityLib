package io.github.mooy1.infinitylib.player;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.Bukkit;
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
@UtilityClass
public final class MessageUtils {

    private static final Map<UUID, Long> coolDowns = new HashMap<>();
    
    static {
        LeaveListener.add(coolDowns);
    }
    
    public static void message(@Nonnull Player p, @Nonnull String... messages) {
        for (String m : messages) {
            p.sendMessage(ChatColors.color(m));
        }
    }
    
    public static void messageWithPrefix(@Nonnull Player p, @Nonnull String... messages) {
        for (String m : messages) {
            p.sendMessage(PluginUtils.getPrefix() + ChatColors.color(m));
        }
    }

    public static void broadcast(@Nonnull String message) {
        Bukkit.broadcastMessage(PluginUtils.getPrefix() + ChatColors.color(message));
    }

    public static void messageWithCD(@Nonnull Player p, long coolDown, @Nonnull String... messages) {
        if (coolDowns.containsKey(p.getUniqueId()) && System.currentTimeMillis() - coolDowns.get(p.getUniqueId()) < coolDown) {
            return;
        }
        coolDowns.put(p.getUniqueId(), System.currentTimeMillis());
        
        message(p, messages);
    }
    
}
