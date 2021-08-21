package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@ParametersAreNonnullByDefault
public final class MachineBlock extends AbstractMachineBlock {

    private final List<MachineBlockRecipe> recipes = new ArrayList<>();

    // TODO ticksPerOutput

    public MachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected boolean process(Block b, BlockMenu menu) {
        HashMap<String, Integer> map = new HashMap<>(1, 1F);
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null && !item.getType().isAir()) {
                map.compute(Slimefun.getItemDataService().getItemData(item).orElseGet(() -> item.getType().name()),
                        (str, amt) -> amt == null ? item.getAmount() : amt + item.getAmount());
            }
        }

        recipeLoop: for (MachineBlockRecipe recipe : this.recipes) {
            for (int i = 0; i < recipe.strings.length; i++) {
                if (map.getOrDefault(recipe.strings[i], 0) < recipe.amounts[i]) {
                    continue recipeLoop;
                }
            }

            // TODO handle output
            break;
        }

        return true;
    }

    public MachineBlock addRecipe(ItemStack output, ItemStack... inputs) {
        MachineBlockRecipe recipe = new MachineBlockRecipe(output);
        for (ItemStack item : inputs) {
            recipe.add(item);
        }
        if (recipe.isValid()) {
            this.recipes.add(recipe);
        }
        return this;
    }

    public MachineBlock useRecipes(MachineRecipeType type) {
        type.subscribe((inputs, outputs) -> this.addRecipe(outputs, inputs));
        return this;
    }

}

