package io.github.mooy1.infinitylib.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;

/**
 * Collection of utils for modifying item lore
 * 
 * @author Mooy1
 */
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
        List<String> lore;
        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        for (String s : lores) {
            lore.add(ChatColors.color(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void setLore(@Nonnull ItemStack item, @Nonnull List<String> list) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String s : list) {
            lore.add(ChatColors.color(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void addLore(@Nonnull ItemStack item, @Nonnull List<String> list) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;
        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        for (String s : list) {
            lore.add(ChatColors.color(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    
    public static void replaceLine(@Nonnull ItemStack item, String target, String replace) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                for (int i = 0 ; i < lore.size() ; i++) {
                    if (lore.get(i).equals(target)) {
                        lore.set(i, replace);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        return;
                    }
                }
            }
        }
    }
    
}
