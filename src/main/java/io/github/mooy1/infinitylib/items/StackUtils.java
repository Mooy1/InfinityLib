package io.github.mooy1.infinitylib.items;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Collection of utils for modifying ItemStacks and getting their ids
 *
 * @author Mooy1
 */
@UtilityClass
public final class StackUtils {

    private static final NamespacedKey ID = SlimefunPlugin.getItemDataService().getKey();

    @Nullable
    public static String getID(@Nonnull ItemStack item) {
        if (item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemId();
        }
        if (!item.hasItemMeta()) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer().get(ID, PersistentDataType.STRING);
    }
    
    @Nonnull
    public static String getIDorType(@Nonnull ItemStack item) {
        String id = getID(item);
        if (id == null) {
            return item.getType().toString();
        } else {
            return id;
        }
    }

    @Nullable
    public static ItemStack getItemByID(@Nonnull String id) {
        return getItemByID(id, 1);
    }

    @Nullable
    public static ItemStack getItemByID(@Nonnull String id, @Range(from = 1, to = 64) int amount) {
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
            return null;
        }
    }

    @Nullable
    public static ItemStack getItemByIDorType(@Nonnull String id) {
        return getItemByIDorType(id, 1);
    }

    @Nullable
    public static ItemStack getItemByIDorType(@Nonnull String id, @Range(from = 1, to = 64) int amount) {
        SlimefunItem sfItem = SlimefunItem.getByID(id);
        if (sfItem != null) {
            return new CustomItem(sfItem.getItem(), amount);
        } else {
            Material material = Material.getMaterial(id);
            if (material != null){
                return new ItemStack(material, amount);
            } else {
                return null;
            }
        }
    }
    
    @Nonnull
    public static ItemStack removeEnchants(@Nonnull ItemStack item) {
        for (Enchantment e : item.getEnchantments().keySet()) {
            item.removeEnchantment(e);
        }
        return item;
    }
    
    @Nonnull
    public static ItemStack damageItem(@Nonnull Player p, @Nonnull ItemStack item, int tries) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            
            int damage;

            int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
            if (unbreakingLevel > 0) {
                damage = 0;
                double threshold = 60 + Math.floorDiv(40, (unbreakingLevel + 1));
                while (tries --> 0) {
                    if (ThreadLocalRandom.current().nextDouble(100) <= threshold) {
                        damage++;
                    }
                }
            } else {
                damage = tries;
            }
            
            if (damage != 0) {
                Damageable damageable = (Damageable) meta;

                if (damageable.getDamage() + damage >= item.getType().getMaxDurability()) {
                    p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    item.setAmount(0);
                } else {
                    damageable.setDamage(damageable.getDamage() + damage);
                    item.setItemMeta(meta);
                }
            }
        }
        return item;
    }
    
    private static Method copyCBItemStackToNMS;
    private static Method itemStackNameComponent;
    private static Method componentToString;

    static {
        try {
            Field field = ItemUtils.class.getDeclaredField("copy");
            field.setAccessible(true);
            copyCBItemStackToNMS = (Method) field.get(null);
            copyCBItemStackToNMS.setAccessible(true);
            field = ItemUtils.class.getDeclaredField("getName");
            field.setAccessible(true);
            itemStackNameComponent = (Method) field.get(null);
            itemStackNameComponent.setAccessible(true);
            field = ItemUtils.class.getDeclaredField("toString");
            field.setAccessible(true);
            componentToString = (Method) field.get(null);
            componentToString.setAccessible(true);
        } catch (Exception e) {
            PluginUtils.log(Level.SEVERE, "Failed to load ItemStack name methods!");
            e.printStackTrace();
        }
    }
    
    public static String getInternalName(@Nonnull ItemStack item) {
        try {
            return ChatColor.WHITE + (String) componentToString.invoke(itemStackNameComponent.invoke(copyCBItemStackToNMS.invoke(null, item)));
        } catch (Exception e) {
            PluginUtils.log(Level.SEVERE, "Failed to get ItemStack name for " + item.toString());
            e.printStackTrace();
            return ChatColor.RED + "ERROR";
        }
    }
    
    public static String getDisplayName(@Nonnull ItemStack item, @Nonnull ItemMeta meta) {
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return getInternalName(item);
    }

    public static String getDisplayName(@Nonnull ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                return meta.getDisplayName();
            }
        }
        return getInternalName(item);
    }
    
}
