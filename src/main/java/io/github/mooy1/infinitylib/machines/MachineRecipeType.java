package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public final class MachineRecipeType extends RecipeType {

    @Getter
    private final Map<ItemStack[], ItemStack> recipes = new LinkedHashMap<>();
    private final List<BiConsumer<ItemStack[], ItemStack>> callbacks = new ArrayList<>();

    public MachineRecipeType(String key, ItemStack item) {
        super(AbstractAddon.createKey(key), item);
    }

    @Override
    public void register(ItemStack[] recipe, ItemStack result) {
        callbacks.forEach(c -> c.accept(recipe, result));
        recipes.put(recipe, result);
    }

    public void sendRecipesTo(BiConsumer<ItemStack[], ItemStack> callback) {
        recipes.forEach(callback);
        callbacks.add(callback);
    }

}
