package io.github.mooy1.infinitylib.items;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CachedItemStack {

    @Getter
    private final ItemStack item;
    private Optional<ItemMeta> meta;
    private Optional<String> id;

    public CachedItemStack(@Nonnull ItemStack item) {
        this.item = item;
    }

    @Nullable
    public String getID() {
        return cacheID().orElse(null);
    }

    @Nonnull
    public String getIDorType() {
        return cacheID().orElseGet(() -> this.item.getType().toString());
    }

    @Nonnull
    private Optional<String> cacheID() {
        if (this.id == null) {
            if (hasMeta()) {
                return this.id = Optional.ofNullable(StackUtils.getID(getMeta()));
            } else {
                return this.id = Optional.empty();
            }
        }
        return this.id;
    }

    @Nonnull
    public ItemMeta getMeta() {
        if (this.meta == null) {
            if (this.item.hasItemMeta()) {
                this.meta = Optional.ofNullable(this.item.getItemMeta());
            } else {
                this.meta = Optional.empty();
            }
        }
        return this.meta.orElseGet(this.item::getItemMeta);
    }

    public boolean hasMeta() {
        if (this.meta == null) {
            return this.item.hasItemMeta();
        }
        return this.meta.isPresent();
    }

}
