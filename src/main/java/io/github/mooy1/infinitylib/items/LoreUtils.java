package io.github.mooy1.infinitylib.items;

import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class LoreUtils {
    
    public static void setLore(@Nonnull ItemStack item, String... lores) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String s : lores) {
            lore.add(ChatColors.color(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    
    public static void addLore(@Nonnull ItemStack item, @Nonnull String... lores) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        for (String s : lores) {
            lore.add(ChatColors.color(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    
}
