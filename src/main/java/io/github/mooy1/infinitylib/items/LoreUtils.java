package io.github.mooy1.infinitylib.items;

import lombok.NonNull;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LoreUtils {
    
    public static void setLore(@Nonnull ItemStack item, String... lores) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        List<String> lore = new ArrayList<>();

        for (String s : lores) {
            lore.add(ChatColors.color(s));
        }

        meta.setLore(lore);

        item.setItemMeta(meta);
    }
    
    public static void addLore(@Nonnull ItemStack item, @Nonnull String... lores) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        List<String> lore = meta.getLore();

        if (lore != null) {
            for (String s : lores) {
                lore.add(ChatColors.color(s));
            }
        }
        
        meta.setLore(lore);

        item.setItemMeta(meta);
    }
    
    public static void addLore(@Nonnull ItemStack item, @Nonnull List<String> lores) {
        addLore(item, lores.toArray(new String[0]));
    }
    
    public static void removeLore(ItemStack item, int offset, String find, int amount) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;

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
    public static void insertLore(ItemStack item, int offset, String find, List<String> lores) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;

        List<String> lore = meta.getLore();

        if (lore == null)  return;

        int position = lore.size() - 1;
        int i = 0;
        for (String line : lore) {
            if (ChatColor.stripColor(line).contains(ChatColor.stripColor(find))) {
                position = i;
            }
            i++;
        }

        position += offset;

        if (position < 0) return;

        lore = lore.subList(0, position);

        lore.addAll(lores);

        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    @ParametersAreNonnullByDefault
    public static void insertLore(ItemStack item, int offset, String find, String... lores) {
        insertLore(item, offset, find, Arrays.asList(lores));
    }

    /**
     * This method transfers parts of lore from 1 item to another
     *
     * @param output item to transfer to
     * @param input item to transfer from
     * @param key string of lore to look for
     * @param offset index of first line relative to key index
     * @param lines total lines to transfer
     */
    public static void transferLore(@NonNull ItemStack output, @NonNull ItemStack input, @NonNull String key, int offset, int lines) {
        ItemMeta inputMeta = input.getItemMeta();
        if (inputMeta == null) {
            return;
        }
        
        List<String> inputLore = inputMeta.getLore();
        if (inputLore == null) {
            return;
        }

        ItemMeta outputMeta = output.getItemMeta();
        if (outputMeta == null) {
            return;
        }

        List<String> outputLore = outputMeta.getLore();
        if (outputLore == null) {
            return;
        }

        int i = 0;
        for (String line : inputLore) {
            if (ChatColor.stripColor(line).contains(key)) {

                for (int ii = i + offset ; ii < i + lines + offset ; ii++) {
                    outputLore.add(inputLore.get(ii));
                }
                outputMeta.setLore(outputLore);
                output.setItemMeta(outputMeta);

            }
            i++;
        }
    }

    @Nonnull
    public static ItemStack getDisplayItem(@Nonnull ItemStack output) {
        addLore(output, ChatColor.GREEN + "", ChatColor.GREEN + "-------------------", ChatColor.GREEN + "\u21E8 Click to craft", ChatColor.GREEN + "-------------------");
        return output;
    }
    
}
