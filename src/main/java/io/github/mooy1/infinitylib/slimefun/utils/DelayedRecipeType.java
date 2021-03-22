package io.github.mooy1.infinitylib.slimefun.utils;

import io.github.mooy1.infinitylib.core.PluginUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

/**
 * A recipe type which stores all recipes registered to it for use at a later date.
 * 
 * @author Mooy1
 */
public final class DelayedRecipeType extends RecipeType {
    
    private DelayedConsumer consumer;
    
    public DelayedRecipeType(SlimefunItemStack item) {
        this(item, new DelayedConsumer());
    }
    
    private DelayedRecipeType(SlimefunItemStack item, DelayedConsumer consumer) {
        super(PluginUtils.getKey(item.getItemId().toLowerCase(Locale.ROOT)), item, consumer);
        this.consumer = consumer;
    }
    
    public void accept(BiConsumer<ItemStack[], ItemStack> consumer) {
        if (this.consumer == null) {
            throw new IllegalStateException("The recipes of this RecipeType have already been accepted!");
        }
        for (Pair<ItemStack[], ItemStack> stacks : this.consumer.toBeAccepted) {
            consumer.accept(stacks.getFirstValue(), stacks.getSecondValue());
        }
        this.consumer.consumer = consumer;
        this.consumer.toBeAccepted = null;
        this.consumer = null;
    }
    
    private static final class DelayedConsumer implements BiConsumer<ItemStack[], ItemStack> {

        private List<Pair<ItemStack[], ItemStack>> toBeAccepted = new ArrayList<>();
        private BiConsumer<ItemStack[], ItemStack> consumer = null;

        @Override
        public void accept(ItemStack[] stacks, ItemStack itemStack) {
            if (this.consumer == null) {
                this.toBeAccepted.add(new Pair<>(stacks, itemStack));
            } else {
                this.consumer.accept(stacks, itemStack);
            }
        }

    }

}
