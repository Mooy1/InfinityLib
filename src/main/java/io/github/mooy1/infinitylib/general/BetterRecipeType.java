package io.github.mooy1.infinitylib.general;

import io.github.mooy1.infinitylib.PluginUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class BetterRecipeType extends RecipeType {
    
    private List<Pair<ItemStack[], ItemStack>> items;
    
    public BetterRecipeType(SlimefunItemStack item) {
        this(item, new ArrayList<>());
    }
    
    private BetterRecipeType(SlimefunItemStack item, List<Pair<ItemStack[], ItemStack>> items) {
        super(PluginUtils.getKey(item.getItemId().toLowerCase(Locale.ROOT)), item, ((itemStacks, itemStack) -> items.add(new Pair<>(itemStacks, itemStack))));
        this.items = items;
    }
    
    public void accept(BiConsumer<ItemStack[], ItemStack> consumer) {
        for (Pair<ItemStack[], ItemStack> pair : this.items) {
            consumer.accept(pair.getFirstValue(), pair.getSecondValue());
        }
        this.items = null;
    }

}
