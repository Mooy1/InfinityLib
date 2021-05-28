package io.github.mooy1.infinitylib.items;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class FastItemStack extends ItemStack {

    @Getter
    private final ItemStack original;

    private Optional<ItemMeta> metaCache;
    private Optional<String> idCache;

    @Nonnull
    public static FastItemStack of(@Nonnull ItemStack item) {
        if (item instanceof FastItemStack) {
            return (FastItemStack) item;
        }
        return new FastItemStack(item);
    }

    @Nonnull
    public static FastItemStack[] ofArray(@Nonnull ItemStack[] items) {
        if (items instanceof FastItemStack[]) {
            return (FastItemStack[]) items;
        }
        FastItemStack[] arr = new FastItemStack[items.length];
        for (int i = 0 ; i < items.length ; i++) {
            if (items[i] != null) {
                arr[i] = of(items[i]);
            }
        }
        return arr;
    }

    @Nullable
    public String getID() {
        return cacheID().orElse(null);
    }

    @Nonnull
    public String getIDorType() {
        return cacheID().orElseGet(() -> getType().toString());
    }

    @Nonnull
    private Optional<String> cacheID() {
        if (this.idCache == null) {
            if (hasItemMeta()) {
                return this.idCache = Optional.ofNullable(StackUtils.getID(getItemMeta()));
            } else {
                return this.idCache = Optional.empty();
            }
        }
        return this.idCache;
    }

    public boolean fastEquals(@Nonnull ItemStack item) {
        String id = getID();
        String other = StackUtils.getID(item);
        if (id == null) {
            return other == null && getType() == item.getType();
        } else {
            return id.equals(other);
        }
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public ItemMeta cloneItemMeta() {
        return this.original.getItemMeta();
    }

    @Nonnull
    @Override
    public ItemMeta getItemMeta() {
        if (this.metaCache == null) {
            if (hasItemMeta()) {
                this.metaCache = Optional.of(cloneItemMeta());
            } else {
                this.metaCache = Optional.empty();
            }
        }
        return this.metaCache.orElseGet(this::cloneItemMeta);
    }

    @Override
    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
        this.metaCache = Optional.ofNullable(itemMeta);
        this.idCache = null;
        return this.original.setItemMeta(itemMeta);
    }

    @Override
    public boolean hasItemMeta() {
        if (this.metaCache == null) {
            return this.original.hasItemMeta();
        }
        return this.metaCache.isPresent();
    }

    @Nonnull
    @Override
    public Material getType() {
        return this.original.getType();
    }

    @Override
    public void setType(@Nonnull Material type) {
        this.original.setType(type);
    }

    @Override
    public int getAmount() {
        return this.original.getAmount();
    }

    @Override
    public void setAmount(int amount) {
        this.original.setAmount(amount);
    }

    @Override
    public String toString() {
        return this.original.toString();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return this.original.equals(obj);
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        return this.original.isSimilar(stack);
    }

    @Nonnull
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ItemStack clone() {
        return this.original.clone();
    }

    @Override
    public int hashCode() {
        return this.original.hashCode();
    }

    @Override
    public boolean containsEnchantment(@Nonnull Enchantment ench) {
        return this.original.containsEnchantment(ench);
    }

    @Override
    public int getEnchantmentLevel(@Nonnull Enchantment ench) {
        return this.original.getEnchantmentLevel(ench);
    }

    @Nonnull
    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return this.original.getEnchantments();
    }

    @Override
    public void addUnsafeEnchantment(@Nonnull Enchantment ench, int level) {
        this.original.addUnsafeEnchantment(ench, level);
    }

    @Override
    public int removeEnchantment(@Nonnull Enchantment ench) {
        return this.original.removeEnchantment(ench);
    }

}
