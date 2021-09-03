package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public final class MachineBlock extends AbstractMachineBlock {

    private final List<MachineBlockRecipe> recipes = new ArrayList<>();
    private int ticksPerOutput = -1;

    public MachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    public MachineBlock ticksPerOutput(int ticks) {
        if (ticks < 1) {
            throw new IllegalArgumentException("Ticks Per Output must be at least 1!");
        }
        ticksPerOutput = ticks;
        return this;
    }

    @Override
    public void preRegister() {
        if (ticksPerOutput == -1) {
            throw new IllegalStateException("You must call .ticksPerOutput() before registering!");
        }
        super.preRegister();
    }

    @Override
    protected boolean process(Block b, BlockMenu menu) {
        if (AbstractAddon.slimefunTickCount() % ticksPerOutput != 0) {
            return true;
        }

        Map<String, MachineInput> map = new HashMap<>(2, 1F);
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null) {
                String string = Slimefun.getItemDataService().getItemData(item).orElseGet(() -> item.getType().name());
                map.compute(string, (str, input) -> input == null ? new MachineInput(item) : input.add(item));
            }
        }

        for (MachineBlockRecipe recipe : recipes) {
            if (recipe.check(map)) {
                if (quickPush(recipe.output.clone(), menu)) {
                    recipe.consume(map);
                    updateStatus(menu, PROCESSING_ITEM);
                    return true;
                }
                else {
                    updateStatus(menu, NO_ROOM_ITEM);
                    return false;
                }
            }
        }

        updateStatus(menu, IDLE_ITEM);
        return false;
    }

    public MachineBlock addRecipe(ItemStack output, ItemStack... inputs) {
        if (inputs.length == 0) {
            throw new IllegalArgumentException("Cannot add recipe with no input!");
        }
        MachineBlockRecipe recipe = new MachineBlockRecipe(output);
        for (ItemStack item : inputs) {
            recipe.add(item);
        }
        recipes.add(recipe);
        return this;
    }

    public MachineBlock subscribe(MachineRecipeType type) {
        type.subscribe((inputs, outputs) -> addRecipe(outputs, inputs));
        return this;
    }

}
