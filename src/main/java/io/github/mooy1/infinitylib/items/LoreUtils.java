package io.github.mooy1.infinitylib.items;

import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
    
    public static void removeLore(ItemStack item, int offset, String find, int amount) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        if (lore == null)  return;
        
        int position = lore.size() - 1;
        for (int i = 0 ; i < lore.size() ; i++) {
            if (ChatColor.stripColor(lore.get(i)).contains(ChatColor.stripColor(find))) {
                position = i;
            }
        }

        position += offset;

        if (position < 0) return;

        for (int j = 0 ; j < amount ; j++) {
            lore.remove(position);
        }

        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    @ParametersAreNonnullByDefault
    public static void insertLore(ItemStack item, int offset, String find, String... lores) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        if (lore == null)  return;

        int position = -1;
        for (int i = 0 ; i < lore.size() ; i++) {
            if (ChatColor.stripColor(lore.get(i)).contains(ChatColor.stripColor(find))) {
                position = i;
            }
        }
        
        if (position < 0) return;

        position += offset;
        
        for (String line : lores) {
            lore.add(position, ChatColors.color(line));
        }

        meta.setLore(lore);

        item.setItemMeta(meta);
    }
    
    @Nonnull
    public static ItemStack getDisplayItem(@Nonnull ItemStack output) {
        addLore(output, ChatColor.GREEN + "", ChatColor.GREEN + "-------------------", ChatColor.GREEN + "\u21E8 Click to craft", ChatColor.GREEN + "-------------------");
        return output;
    }
    
}
