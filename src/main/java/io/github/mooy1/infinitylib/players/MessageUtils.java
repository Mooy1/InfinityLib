package io.github.mooy1.infinitylib.players;

import io.github.mooy1.infinitylib.core.PluginUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Collection of utils for sending messages to players and broadcasting
 *
 * @author Mooy1
 * 
 */
@UtilityClass
public final class MessageUtils {

    private static final CoolDownMap coolDowns = new CoolDownMap();
    
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

    public static void messageWithCD(@Nonnull Player p, long coolDown, @Nonnull String... messages) {
        if (coolDowns.checkAndPut(p.getUniqueId(), coolDown)) {
            message(p, messages);
        }
    }

    public static void broadcast(@Nonnull String message) {
        Bukkit.broadcastMessage(PluginUtils.getPrefix() + ChatColors.color(message));
    }
    
}
