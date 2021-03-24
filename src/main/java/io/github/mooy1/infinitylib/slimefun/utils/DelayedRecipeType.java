package io.github.mooy1.infinitylib.slimefun.utils;

import io.github.mooy1.infinitylib.AbstractAddon;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.NamespacedKey;
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

    private final DelayedConsumer consumer;
    
    public DelayedRecipeType(AbstractAddon addon, SlimefunItemStack item) {
        this(addon.getKey(item.getItemId().toLowerCase(Locale.ROOT)), item, new DelayedConsumer());
    }
    
    private DelayedRecipeType(NamespacedKey key, SlimefunItemStack item, DelayedConsumer consumer) {
        super(key, item, consumer);
        this.consumer = consumer;
    }
    
    public void accept(BiConsumer<ItemStack[], ItemStack> consumer) {
        // add to acceptors
        this.consumer.acceptors.add(consumer);
        
        // accept all current
        for (Pair<ItemStack[], ItemStack> pair : this.consumer.toBeAccepted) {
            consumer.accept(pair.getFirstValue(), pair.getSecondValue());
        }
    }
    
    private static final class DelayedConsumer implements BiConsumer<ItemStack[], ItemStack> {

        private final List<BiConsumer<ItemStack[], ItemStack>> acceptors = new ArrayList<>();
        private final List<Pair<ItemStack[], ItemStack>> toBeAccepted = new ArrayList<>();

        @Override
        public void accept(ItemStack[] stacks, ItemStack itemStack) {
            // add to all accepted
            this.toBeAccepted.add(new Pair<>(stacks, itemStack));
            
            // accept to all acceptors
            for (BiConsumer<ItemStack[], ItemStack> acceptor : this.acceptors) {
                acceptor.accept(stacks, itemStack);
            }
        }

    }

}
