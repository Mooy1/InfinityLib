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
public final class CachedItemStack extends ItemStack {

    @Getter
    private final ItemStack cached;
    private Optional<ItemMeta> meta;
    private Optional<String> id;

    @Nonnull
    public static CachedItemStack of(@Nonnull ItemStack item) {
        if (item instanceof CachedItemStack) {
            return (CachedItemStack) item;
        }
        return new CachedItemStack(item);
    }

    @Nonnull
    public static CachedItemStack[] ofArray(@Nonnull ItemStack[] items) {
        if (items instanceof CachedItemStack[]) {
            return (CachedItemStack[]) items;
        }
        CachedItemStack[] arr = new CachedItemStack[items.length];
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
        if (this.id == null) {
            if (hasItemMeta()) {
                return this.id = Optional.ofNullable(StackUtils.getID(getItemMeta()));
            } else {
                return this.id = Optional.empty();
            }
        }
        return this.id;
    }

    public boolean softEquals(@Nonnull ItemStack item) {
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
        return this.cached.getItemMeta();
    }

    @Nonnull
    @Override
    public ItemMeta getItemMeta() {
        if (this.meta == null) {
            if (hasItemMeta()) {
                this.meta = Optional.of(cloneItemMeta());
            } else {
                this.meta = Optional.empty();
            }
        }
        return this.meta.orElseGet(this::cloneItemMeta);
    }

    @Override
    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
        this.meta = Optional.ofNullable(itemMeta);
        this.id = null;
        return this.cached.setItemMeta(itemMeta);
    }

    @Override
    public boolean hasItemMeta() {
        if (this.meta == null) {
            return this.cached.hasItemMeta();
        }
        return this.meta.isPresent();
    }

    @Nonnull
    @Override
    public Material getType() {
        return this.cached.getType();
    }

    @Override
    public void setType(@Nonnull Material type) {
        this.cached.setType(type);
    }

    @Override
    public int getAmount() {
        return this.cached.getAmount();
    }

    @Override
    public void setAmount(int amount) {
        this.cached.setAmount(amount);
    }

    @Override
    public String toString() {
        return this.cached.toString();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return this.cached.equals(obj);
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        return this.cached.isSimilar(stack);
    }

    @Nonnull
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ItemStack clone() {
        return this.cached.clone();
    }

    @Override
    public int hashCode() {
        return this.cached.hashCode();
    }

    @Override
    public boolean containsEnchantment(@Nonnull Enchantment ench) {
        return this.cached.containsEnchantment(ench);
    }

    @Override
    public int getEnchantmentLevel(@Nonnull Enchantment ench) {
        return this.cached.getEnchantmentLevel(ench);
    }

    @Nonnull
    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return this.cached.getEnchantments();
    }

    @Override
    public void addUnsafeEnchantment(@Nonnull Enchantment ench, int level) {
        this.cached.addUnsafeEnchantment(ench, level);
    }

    @Override
    public int removeEnchantment(@Nonnull Enchantment ench) {
        return this.cached.removeEnchantment(ench);
    }

}
